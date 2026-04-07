package com.hoaug.movieapi.modules.auth.infrastructure.persistence.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hoaug.movieapi.modules.auth.infrastructure.persistence.entity.RefreshTokenEntity;

public interface JpaRefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {
  Optional<RefreshTokenEntity> findByToken (String token);

  List<RefreshTokenEntity> findByUserId (Long userId);

  void deleteByExpiresAtBefore (LocalDateTime dateTime);
}
