package com.hoaug.movieapi.modules.auth.infrastructure.persistence.adapter;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.auth.domain.model.PasswordResetToken;
import com.hoaug.movieapi.modules.auth.domain.repository.PasswordResetTokenRepository;
import com.hoaug.movieapi.modules.auth.infrastructure.persistence.entity.PasswordResetTokenEntity;
import com.hoaug.movieapi.modules.auth.infrastructure.persistence.repository.JpaPasswordResetTokenRepository;

@Component
public class PasswordResetTokenRepositoryAdapter implements PasswordResetTokenRepository {

  private final JpaPasswordResetTokenRepository jpaPasswordResetTokenRepository;

  public PasswordResetTokenRepositoryAdapter(
      JpaPasswordResetTokenRepository jpaPasswordResetTokenRepository) {
    this.jpaPasswordResetTokenRepository = jpaPasswordResetTokenRepository;
  }

  @Override
  public PasswordResetToken save (PasswordResetToken token) {
    PasswordResetTokenEntity entity = toEntity(token);
    return toDomain(jpaPasswordResetTokenRepository.save(entity));
  }

  @Override
  public Optional<PasswordResetToken> findByToken (String token) {
    return jpaPasswordResetTokenRepository.findByToken(token).map(this::toDomain);
  }

  private PasswordResetToken toDomain (PasswordResetTokenEntity entity) {
    PasswordResetToken token = new PasswordResetToken();
    token.setId(entity.getId());
    token.setUserId(entity.getUserId());
    token.setToken(entity.getToken());
    token.setExpiresAt(entity.getExpiresAt());
    token.setUsedAt(entity.getUsedAt());
    token.setCreatedAt(entity.getCreatedAt());
    return token;
  }

  private PasswordResetTokenEntity toEntity (PasswordResetToken token) {
    PasswordResetTokenEntity entity = new PasswordResetTokenEntity();
    entity.setId(token.getId());
    entity.setUserId(token.getUserId());
    entity.setToken(token.getToken());
    entity.setExpiresAt(token.getExpiresAt());
    entity.setUsedAt(token.getUsedAt());
    entity.setCreatedAt(token.getCreatedAt());
    return entity;
  }
}
