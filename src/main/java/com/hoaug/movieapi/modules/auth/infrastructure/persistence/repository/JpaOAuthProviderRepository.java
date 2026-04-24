package com.hoaug.movieapi.modules.auth.infrastructure.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hoaug.movieapi.modules.auth.infrastructure.persistence.entity.OAuthProviderEntity;

public interface JpaOAuthProviderRepository extends JpaRepository<OAuthProviderEntity, Long> {
  Optional<OAuthProviderEntity> findByName (String name);
}
