package com.hoaug.movieapi.modules.auth.infrastructure.persistence.adapter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.auth.domain.model.RefreshToken;
import com.hoaug.movieapi.modules.auth.domain.repository.RefreshTokenRepository;
import com.hoaug.movieapi.modules.auth.infrastructure.persistence.entity.RefreshTokenEntity;
import com.hoaug.movieapi.modules.auth.infrastructure.persistence.repository.JpaRefreshTokenRepository;

@Component
public class RefreshTokenRepositoryAdapter implements RefreshTokenRepository {

  private final JpaRefreshTokenRepository jpaRefreshTokenRepository;

  public RefreshTokenRepositoryAdapter(JpaRefreshTokenRepository jpaRefreshTokenRepository) {
    this.jpaRefreshTokenRepository = jpaRefreshTokenRepository;
  }

  @Override
  public RefreshToken save (RefreshToken refreshToken) {
    RefreshTokenEntity entity = toEntity(refreshToken);
    return toDomain(jpaRefreshTokenRepository.save(entity));
  }

  @Override
  public Optional<RefreshToken> findByToken (String token) {
    return jpaRefreshTokenRepository.findByToken(token).map(this::toDomain);
  }

  @Override
  public List<RefreshToken> findByUserId (Long userId) {
    return jpaRefreshTokenRepository.findByUserId(userId).stream().map(this::toDomain).toList();
  }

  @Override
  public void deleteExpiredTokens (LocalDateTime dateTime) {
    jpaRefreshTokenRepository.deleteByExpiresAtBefore(dateTime);
  }

  private RefreshToken toDomain (RefreshTokenEntity entity) {
    RefreshToken token = new RefreshToken();
    token.setId(entity.getId());
    token.setUserId(entity.getUserId());
    token.setToken(entity.getToken());
    token.setExpiresAt(entity.getExpiresAt());
    token.setRevokedAt(entity.getRevokedAt());
    token.setCreatedAt(entity.getCreatedAt());
    return token;
  }

  private RefreshTokenEntity toEntity (RefreshToken token) {
    RefreshTokenEntity entity = new RefreshTokenEntity();
    entity.setId(token.getId());
    entity.setUserId(token.getUserId());
    entity.setToken(token.getToken());
    entity.setExpiresAt(token.getExpiresAt());
    entity.setRevokedAt(token.getRevokedAt());
    entity.setCreatedAt(token.getCreatedAt());
    return entity;
  }
}
