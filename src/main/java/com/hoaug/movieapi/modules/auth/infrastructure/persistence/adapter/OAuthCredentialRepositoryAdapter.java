package com.hoaug.movieapi.modules.auth.infrastructure.persistence.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Component;

import tools.jackson.databind.ObjectMapper;
import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.auth.domain.model.OAuthCredential;
import com.hoaug.movieapi.modules.auth.domain.repository.OAuthCredentialRepository;
import com.hoaug.movieapi.modules.auth.infrastructure.persistence.entity.OAuthCredentialEntity;
import com.hoaug.movieapi.modules.auth.infrastructure.persistence.entity.OAuthProviderEntity;
import com.hoaug.movieapi.modules.auth.infrastructure.persistence.repository.JpaOAuthCredentialRepository;
import com.hoaug.movieapi.modules.auth.infrastructure.persistence.repository.JpaOAuthProviderRepository;

@Component
public class OAuthCredentialRepositoryAdapter implements OAuthCredentialRepository {

  private final JpaOAuthCredentialRepository jpaOAuthCredentialRepository;
  private final JpaOAuthProviderRepository jpaOAuthProviderRepository;
  private final ObjectMapper objectMapper;

  public OAuthCredentialRepositoryAdapter(JpaOAuthCredentialRepository jpaOAuthCredentialRepository,
      JpaOAuthProviderRepository jpaOAuthProviderRepository, ObjectMapper objectMapper) {
    this.jpaOAuthCredentialRepository = jpaOAuthCredentialRepository;
    this.jpaOAuthProviderRepository = jpaOAuthProviderRepository;
    this.objectMapper = objectMapper;
  }

  @Override
  public OAuthCredential save (OAuthCredential credential) {
    OAuthCredentialEntity entity = toEntity(credential);
    return toDomain(jpaOAuthCredentialRepository.save(entity));
  }

  @Override
  public Optional<OAuthCredential> findById (Long id) {
    return jpaOAuthCredentialRepository.findById(id).map(this::toDomain);
  }

  @Override
  public Optional<OAuthCredential> findByUserIdAndProvider (Long userId, String provider) {
    Optional<OAuthProviderEntity> providerEntity = jpaOAuthProviderRepository.findByName(provider);
    if (providerEntity.isEmpty()) {
      return Optional.empty();
    }
    return jpaOAuthCredentialRepository
        .findByUserIdAndProviderId(userId, providerEntity.get().getId()).map(this::toDomain);
  }

  @Override
  public Optional<OAuthCredential> findByOauthIdAndProvider (String oauthId, String provider) {
    Optional<OAuthProviderEntity> providerEntity = jpaOAuthProviderRepository.findByName(provider);
    if (providerEntity.isEmpty()) {
      return Optional.empty();
    }
    return jpaOAuthCredentialRepository
        .findByOauthIdAndProviderId(oauthId, providerEntity.get().getId()).map(this::toDomain);
  }

  @Override
  public List<OAuthCredential> findByUserId (Long userId) {
    return jpaOAuthCredentialRepository.findByUserId(userId).stream().map(this::toDomain).toList();
  }

  @Override
  public List<OAuthCredential> findActiveCredentialsByUserId (Long userId) {
    return jpaOAuthCredentialRepository.findActiveByUserId(userId).stream().map(this::toDomain)
        .toList();
  }

  @Override
  public void delete (OAuthCredential credential) {
    if (credential.getId() != null) {
      jpaOAuthCredentialRepository.deleteById(credential.getId());
    }
  }

  @Override
  public void deleteById (Long id) {
    jpaOAuthCredentialRepository.deleteById(id);
  }

  @Override
  public boolean existsByOauthIdAndProvider (String oauthId, String provider) {
    OAuthProviderEntity providerEntity = jpaOAuthProviderRepository.findByName(provider)
        .orElse(null);
    if (providerEntity == null) {
      return false;
    }
    return jpaOAuthCredentialRepository.existsByOauthIdAndProviderId(oauthId,
        providerEntity.getId());
  }

  private OAuthCredential toDomain (OAuthCredentialEntity entity) {
    OAuthCredential credential = new OAuthCredential();
    credential.setId(entity.getId());
    credential.setUserId(entity.getUserId());
    credential.setProvider(entity.getProvider() != null ? entity.getProvider().getName() : null);
    credential.setOauthId(entity.getOauthId());
    credential.setAccessToken(entity.getAccessToken());
    credential.setRefreshToken(entity.getRefreshToken());
    credential.setTokenExpiry(entity.getTokenExpiry());
    credential.setIdToken(entity.getIdToken());
    credential.setConnectedAt(entity.getConnectedAt());
    credential.setLastUsedAt(entity.getLastUsedAt());
    credential.setActive(entity.getIsActive() != null ? entity.getIsActive() : true);

    if (entity.getProfileData() != null && !entity.getProfileData().isEmpty()) {
      try {
        Map<String, Object> profileData = objectMapper.readValue(entity.getProfileData(),
            new tools.jackson.core.type.TypeReference<Map<String, Object>>() {
            });
        credential.setProfileData(profileData);
      } catch (Exception e) {
        credential.setProfileData(new HashMap<>());
      }
    }

    return credential;
  }

  private OAuthCredentialEntity toEntity (OAuthCredential credential) {
    OAuthCredentialEntity entity = new OAuthCredentialEntity();
    entity.setId(credential.getId());
    entity.setUserId(credential.getUserId());

    
    if (credential.getProvider() != null) {
      OAuthProviderEntity provider = jpaOAuthProviderRepository.findByName(credential.getProvider())
          .orElseThrow( () -> new AppException(ErrorCode.VALIDATION_ERROR));
      entity.setProvider(provider);
    }

    entity.setOauthId(credential.getOauthId());
    entity.setAccessToken(credential.getAccessToken());
    entity.setRefreshToken(credential.getRefreshToken());
    entity.setTokenExpiry(credential.getTokenExpiry());
    entity.setIdToken(credential.getIdToken());
    entity.setConnectedAt(credential.getConnectedAt());
    entity.setLastUsedAt(credential.getLastUsedAt());
    entity.setIsActive(credential.isActive());

    if (credential.getProfileData() != null && !credential.getProfileData().isEmpty()) {
      try {
        String profileDataJson = objectMapper.writeValueAsString(credential.getProfileData());
        entity.setProfileData(profileDataJson);
      } catch (Exception e) {
        entity.setProfileData("{}");
      }
    }

    return entity;
  }
}
