package com.hoaug.movieapi.modules.auth.application.usecase;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.auth.application.dto.response.AuthResponse;
import com.hoaug.movieapi.modules.auth.domain.model.OAuthCredential;
import com.hoaug.movieapi.modules.auth.domain.model.RefreshToken;
import com.hoaug.movieapi.modules.auth.domain.repository.AuthUserRepository;
import com.hoaug.movieapi.modules.auth.domain.repository.OAuthCredentialRepository;
import com.hoaug.movieapi.modules.auth.domain.repository.RefreshTokenRepository;
import com.hoaug.movieapi.modules.auth.domain.service.TokenService;
import com.hoaug.movieapi.modules.user.domain.model.AccountStatus;
import com.hoaug.movieapi.modules.user.domain.model.Role;
import com.hoaug.movieapi.modules.user.domain.model.User;

@Component
public class ExchangeOAuthTokenUseCase {
  private final AuthUserRepository authUserRepository;
  private final OAuthCredentialRepository oauthCredentialRepository;
  private final RefreshTokenRepository refreshTokenRepository;
  private final TokenService tokenService;
  private final PasswordEncoder passwordEncoder;

  public ExchangeOAuthTokenUseCase(AuthUserRepository authUserRepository,
      OAuthCredentialRepository oauthCredentialRepository,
      RefreshTokenRepository refreshTokenRepository, TokenService tokenService,
      PasswordEncoder passwordEncoder) {
    this.authUserRepository = authUserRepository;
    this.oauthCredentialRepository = oauthCredentialRepository;
    this.refreshTokenRepository = refreshTokenRepository;
    this.tokenService = tokenService;
    this.passwordEncoder = passwordEncoder;
  }

  public AuthResponse execute (String provider, String oauthId, Map<String, Object> userInfo,
      String accessToken, String refreshToken, LocalDateTime tokenExpiry, String idToken) {
    if (provider == null || provider.isBlank() || oauthId == null || oauthId.isBlank()) {
      throw new AppException(ErrorCode.VALIDATION_ERROR);
    }

    String email = getRequiredString(userInfo, "email");
    String fullName = (String) userInfo.get("name");
    String profilePictureUrl = (String) userInfo.get("picture");

    var existingCredential = oauthCredentialRepository.findByOauthIdAndProvider(oauthId, provider);

    if (existingCredential.isPresent()) {
      OAuthCredential credential = existingCredential.get();
      User user = authUserRepository.findById(credential.getUserId())
          .orElseThrow( () -> new AppException(ErrorCode.USER_NOT_FOUND));

      validateActiveUser(user);
      updateCredential(credential, accessToken, refreshToken, tokenExpiry, idToken, userInfo);

      return generateAuthResponse(user);
    }

    User existingUser = authUserRepository.findByEmail(email).orElse(null);

    if (existingUser == null) {
      User newUser = createUserFromOAuth(email, fullName, profilePictureUrl, provider, oauthId);
      newUser = authUserRepository.save(newUser);

      createCredential(newUser.getId(), provider, oauthId, accessToken, refreshToken, tokenExpiry,
          idToken, userInfo);

      return generateAuthResponse(newUser);
    }

    validateActiveUser(existingUser);

    var linkedCredential = oauthCredentialRepository.findByUserIdAndProvider(existingUser.getId(),
        provider);

    if (linkedCredential.isPresent()) {
      OAuthCredential credential = linkedCredential.get();

      if (!oauthId.equals(credential.getOauthId())) {
        throw new AppException(ErrorCode.VALIDATION_ERROR);
      }

      updateCredential(credential, accessToken, refreshToken, tokenExpiry, idToken, userInfo);
      return generateAuthResponse(existingUser);
    }

    createCredential(existingUser.getId(), provider, oauthId, accessToken, refreshToken,
        tokenExpiry, idToken, userInfo);

    return generateAuthResponse(existingUser);
  }

  private User createUserFromOAuth (String email, String fullName, String profilePictureUrl,
      String provider, String oauthId) {
    User user = new User();
    user.setEmail(email);
    user.setFullName(fullName != null && !fullName.isBlank() ? fullName : email.split("@")[0]);
    user.setUsername(email.split("@")[0]);
    user.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
    user.setAvatarUrl(profilePictureUrl);
    user.setProfilePictureUrl(profilePictureUrl);
    user.setOauthId(oauthId);
    user.setOauthProvider(provider);
    user.setRole(Role.ROLE_USER);
    user.setAccountStatus(AccountStatus.ACTIVE);
    user.setCreatedAt(LocalDateTime.now());
    user.setUpdatedAt(LocalDateTime.now());
    return user;
  }

  private OAuthCredential createCredential (Long userId, String provider, String oauthId,
      String accessToken, String refreshToken, LocalDateTime tokenExpiry, String idToken,
      Map<String, Object> userInfo) {
    OAuthCredential credential = new OAuthCredential();
    credential.setUserId(userId);
    credential.setProvider(provider);
    credential.setOauthId(oauthId);
    credential.setAccessToken(accessToken);
    credential.setRefreshToken(refreshToken);
    credential.setTokenExpiry(tokenExpiry);
    credential.setIdToken(idToken);
    credential.setProfileData(sanitizeProfileData(userInfo));
    credential.setConnectedAt(LocalDateTime.now());
    credential.setLastUsedAt(LocalDateTime.now());
    credential.setActive(true);
    return oauthCredentialRepository.save(credential);
  }

  private void updateCredential (OAuthCredential credential, String accessToken,
      String refreshToken, LocalDateTime tokenExpiry, String idToken,
      Map<String, Object> userInfo) {
    credential.updateLastUsed();
    credential.setAccessToken(accessToken);
    credential.setTokenExpiry(tokenExpiry);
    credential.setIdToken(idToken);
    credential.setProfileData(sanitizeProfileData(userInfo));
    credential.setActive(true);

    if (refreshToken != null && !refreshToken.isBlank()) {
      credential.setRefreshToken(refreshToken);
    }

    oauthCredentialRepository.save(credential);
  }

  private void validateActiveUser (User user) {
    if (user.getAccountStatus() != AccountStatus.ACTIVE) {
      throw new AppException(ErrorCode.ACCOUNT_NOT_ACTIVE);
    }
  }

  private String getRequiredString (Map<String, Object> userInfo, String key) {
    Object value = userInfo.get(key);

    if (value == null || String.valueOf(value).isBlank()) {
      throw new AppException(ErrorCode.VALIDATION_ERROR);
    }

    return String.valueOf(value);
  }

  private Map<String, Object> sanitizeProfileData (Map<String, Object> userInfo) {
    Map<String, Object> profileData = new HashMap<>(userInfo);
    profileData.remove("accessToken");
    profileData.remove("refreshToken");
    profileData.remove("idToken");
    profileData.remove("tokenExpiry");
    return profileData;
  }

  private AuthResponse generateAuthResponse (User user) {
    LocalDateTime now = LocalDateTime.now();

    user.setLastLoginAt(now);
    user.setUpdatedAt(now);
    authUserRepository.save(user);

    String accessToken = tokenService.generateAccessToken(user.getUsername());
    String refreshTokenValue = tokenService.generateRefreshToken();

    RefreshToken refreshToken = new RefreshToken();
    refreshToken.setUserId(user.getId());
    refreshToken.setToken(refreshTokenValue);
    refreshToken.setExpiresAt(now.plusDays(30));
    refreshToken.setCreatedAt(now);
    refreshTokenRepository.save(refreshToken);

    AuthResponse response = new AuthResponse();
    response.setAccessToken(accessToken);
    response.setRefreshToken(refreshTokenValue);
    response.setTokenType("Bearer");
    response.setUserId(user.getId());
    response.setUsername(user.getUsername());
    response.setEmail(user.getEmail());
    response.setFullName(user.getFullName());
    response.setRole(user.getRole());

    return response;
  }
}