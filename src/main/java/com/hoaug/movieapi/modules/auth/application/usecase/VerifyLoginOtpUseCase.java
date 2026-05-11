package com.hoaug.movieapi.modules.auth.application.usecase;

import java.time.LocalDateTime;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.auth.application.dto.request.VerifyOtpRequest;
import com.hoaug.movieapi.modules.auth.application.dto.response.AuthResponse;
import com.hoaug.movieapi.modules.auth.application.service.AuthOtpService;
import com.hoaug.movieapi.modules.auth.domain.model.AuthOtpChallenge;
import com.hoaug.movieapi.modules.auth.domain.model.AuthOtpPurpose;
import com.hoaug.movieapi.modules.auth.domain.model.RefreshToken;
import com.hoaug.movieapi.modules.auth.domain.repository.AuthUserRepository;
import com.hoaug.movieapi.modules.auth.domain.repository.RefreshTokenRepository;
import com.hoaug.movieapi.modules.auth.domain.service.TokenService;
import com.hoaug.movieapi.modules.user.domain.model.User;


@Component
public class VerifyLoginOtpUseCase {
  private final AuthOtpService authOtpService;
  private final AuthUserRepository authUserRepository;
  private final RefreshTokenRepository refreshTokenRepository;
  private final TokenService tokenService;
  private final TrustedDeviceUseCase trustedDeviceUseCase;

  public record Result(AuthResponse authResponse, ResponseCookie trustedDeviceCookie) {
  }

  public VerifyLoginOtpUseCase(AuthOtpService authOtpService,
      AuthUserRepository authUserRepository, RefreshTokenRepository refreshTokenRepository,
      TokenService tokenService, TrustedDeviceUseCase trustedDeviceUseCase) {
    this.authOtpService = authOtpService;
    this.authUserRepository = authUserRepository;
    this.refreshTokenRepository = refreshTokenRepository;
    this.tokenService = tokenService;
    this.trustedDeviceUseCase = trustedDeviceUseCase;
  }

  public Result execute (VerifyOtpRequest request) {
    AuthOtpChallenge challenge = authOtpService.verify(AuthOtpPurpose.LOGIN,
        request.getChallengeToken(), request.getOtp());

    User user = authUserRepository.findById(challenge.getUserId())
        .orElseThrow( () -> new AppException(ErrorCode.USER_NOT_FOUND));

    LocalDateTime now = LocalDateTime.now();

    String accessToken = tokenService.generateAccessToken(user.getUsername());
    String refreshTokenValue = tokenService.generateRefreshToken();

    RefreshToken refreshToken = new RefreshToken();
    refreshToken.setUserId(user.getId());
    refreshToken.setToken(refreshTokenValue);
    refreshToken.setExpiresAt(now.plusDays(30));
    refreshToken.setCreatedAt(now);
    refreshTokenRepository.save(refreshToken);

    ResponseCookie trustedDeviceCookie = request.isRememberMe() ? trustedDeviceUseCase.trust(user.getId()) : null;

    AuthResponse response = new AuthResponse();
    response.setAccessToken(accessToken);
    response.setRefreshToken(refreshTokenValue);
    response.setTokenType("Bearer");
    response.setUserId(user.getId());
    response.setUsername(user.getUsername());
    response.setEmail(user.getEmail());
    response.setFullName(user.getFullName());
    response.setRole(user.getRole());
    return new Result(response, trustedDeviceCookie);
  }
}
