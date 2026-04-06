package com.hoaug.movieapi.modules.auth.infrastructure.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hoaug.movieapi.modules.auth.infrastructure.persistence.entity.PasswordResetTokenEntity;

public interface JpaPasswordResetTokenRepository
    extends JpaRepository<PasswordResetTokenEntity, Long> {
  Optional<PasswordResetTokenEntity> findByToken (String token);
}
