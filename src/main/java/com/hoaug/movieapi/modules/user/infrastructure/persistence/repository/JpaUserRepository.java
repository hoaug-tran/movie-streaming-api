package com.hoaug.movieapi.modules.user.infrastructure.persistence.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.hoaug.movieapi.modules.user.infrastructure.persistence.entity.UserEntity;

public interface JpaUserRepository extends JpaRepository<UserEntity, Long> {
  Optional<UserEntity> findByUsername (String username);

  Optional<UserEntity> findByEmail (String email);

  boolean existsByUsername (String username);

  boolean existsByEmail (String email);

  long countByAccountStatus (
      com.hoaug.movieapi.modules.user.domain.model.AccountStatus accountStatus);

  long countByRole (com.hoaug.movieapi.modules.user.domain.model.Role role);

  long countByCreatedAtAfter (java.time.LocalDateTime createdAt);

  long countByLastLoginAtAfter (java.time.LocalDateTime lastLoginAt);

  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Transactional
  @Query("UPDATE UserEntity u SET u.premiumExpiryDate = :expiryDate WHERE u.id = :userId")
  int updatePremiumExpiryDate (@Param("userId") Long userId,
      @Param("expiryDate") LocalDateTime expiryDate);
}
