package com.hoaug.movieapi.modules.auth.infrastructure.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hoaug.movieapi.modules.auth.infrastructure.persistence.entity.OAuthCredentialEntity;

public interface JpaOAuthCredentialRepository extends JpaRepository<OAuthCredentialEntity, Long> {
  Optional<OAuthCredentialEntity> findByUserIdAndProviderId (Long userId, Long providerId);

  Optional<OAuthCredentialEntity> findByOauthIdAndProviderId (String oauthId, Long providerId);

  List<OAuthCredentialEntity> findByUserId (Long userId);

  @Query("SELECT o FROM OAuthCredentialEntity o WHERE o.userId = :userId AND o.isActive = true")
  List<OAuthCredentialEntity> findActiveByUserId (@Param("userId") Long userId);

  boolean existsByOauthIdAndProviderId (String oauthId, Long providerId);

  void deleteByUserIdAndProviderId (Long userId, Long providerId);
}
