package com.hoaug.movieapi.modules.auth.infrastructure.oauth;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.auth.application.dto.oauth.GoogleOAuthUserInfo;
import com.hoaug.movieapi.modules.auth.application.port.GoogleOAuthClient;
import com.hoaug.movieapi.modules.auth.infrastructure.persistence.entity.OAuthProviderEntity;
import com.hoaug.movieapi.modules.auth.infrastructure.persistence.repository.JpaOAuthProviderRepository;

@Component
public class GoogleOAuthClientAdapter implements GoogleOAuthClient {
  private final JpaOAuthProviderRepository jpaOAuthProviderRepository;
  private final RestTemplate restTemplate = new RestTemplate();

  public GoogleOAuthClientAdapter(JpaOAuthProviderRepository jpaOAuthProviderRepository) {
    this.jpaOAuthProviderRepository = jpaOAuthProviderRepository;
  }

  @Override
  public GoogleOAuthUserInfo exchangeCode (String code) {
    OAuthProviderEntity provider = getProvider();

    Map<String, Object> tokenBody = exchangeCodeForToken(provider, code);
    String accessToken = getRequiredString(tokenBody, "access_token");

    Map<String, Object> userInfoBody = fetchUserInfo(provider, accessToken);

    String sub = getRequiredString(userInfoBody, "sub");
    String email = getRequiredString(userInfoBody, "email");

    if (!isEmailVerified(userInfoBody)) {
      throw new AppException(ErrorCode.VALIDATION_ERROR);
    }

    GoogleOAuthUserInfo userInfo = new GoogleOAuthUserInfo();
    userInfo.setSub(sub);
    userInfo.setEmail(email);
    userInfo.setName(getNullableString(userInfoBody, "name"));
    userInfo.setPicture(getNullableString(userInfoBody, "picture"));
    userInfo.setAccessToken(accessToken);
    userInfo.setRefreshToken(getNullableString(tokenBody, "refresh_token"));
    userInfo.setIdToken(getNullableString(tokenBody, "id_token"));
    userInfo.setTokenExpiry(getTokenExpiry(tokenBody));
    userInfo.setProfileData(userInfoBody);

    return userInfo;
  }

  private OAuthProviderEntity getProvider () {
    OAuthProviderEntity provider = jpaOAuthProviderRepository.findByName("google")
        .orElseThrow( () -> new AppException(ErrorCode.VALIDATION_ERROR));

    if (!Boolean.TRUE.equals(provider.getIsActive())) {
      throw new AppException(ErrorCode.VALIDATION_ERROR);
    }

    return provider;
  }

  private Map<String, Object> exchangeCodeForToken (OAuthProviderEntity provider, String code) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
    body.add("code", code);
    body.add("client_id", provider.getClientId());
    body.add("client_secret", provider.getClientSecret());
    body.add("redirect_uri", provider.getRedirectUri());
    body.add("grant_type", "authorization_code");

    HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);

    try {
      ResponseEntity<Map<String, Object>> response = restTemplate.exchange(provider.getTokenUri(),
          HttpMethod.POST, entity, new ParameterizedTypeReference<Map<String, Object>>() {
          });

      return requireBody(response);
    } catch (RestClientException e) {
      throw new AppException(ErrorCode.VALIDATION_ERROR);
    }
  }

  private Map<String, Object> fetchUserInfo (OAuthProviderEntity provider, String accessToken) {
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(accessToken);

    HttpEntity<Void> entity = new HttpEntity<>(headers);

    try {
      ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
          provider.getUserInfoUri(), HttpMethod.GET, entity,
          new ParameterizedTypeReference<Map<String, Object>>() {
          });

      return requireBody(response);
    } catch (RestClientException e) {
      throw new AppException(ErrorCode.VALIDATION_ERROR);
    }
  }

  private Map<String, Object> requireBody (ResponseEntity<Map<String, Object>> response) {
    if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
      throw new AppException(ErrorCode.VALIDATION_ERROR);
    }

    return response.getBody();
  }

  private String getRequiredString (Map<String, Object> body, String key) {
    Object value = body.get(key);

    if (value == null || String.valueOf(value).isBlank()) {
      throw new AppException(ErrorCode.VALIDATION_ERROR);
    }

    return String.valueOf(value);
  }

  private String getNullableString (Map<String, Object> body, String key) {
    Object value = body.get(key);

    if (value == null) {
      return null;
    }

    return String.valueOf(value);
  }

  private LocalDateTime getTokenExpiry (Map<String, Object> tokenBody) {
    Object expiresIn = tokenBody.get("expires_in");

    if (expiresIn == null) {
      return null;
    }

    if (expiresIn instanceof Number number) {
      return LocalDateTime.now().plusSeconds(number.longValue());
    }

    return LocalDateTime.now().plusSeconds(Long.parseLong(String.valueOf(expiresIn)));
  }

  private boolean isEmailVerified (Map<String, Object> userInfoBody) {
    Object value = userInfoBody.get("email_verified");

    if (value == null) {
      return true;
    }

    if (value instanceof Boolean verified) {
      return verified;
    }

    return Boolean.parseBoolean(String.valueOf(value));
  }
}