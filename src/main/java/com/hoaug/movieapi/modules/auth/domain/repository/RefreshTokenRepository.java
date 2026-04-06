package com.hoaug.movieapi.modules.auth.domain.repository;

import java.util.List;
import java.util.Optional;

import com.hoaug.movieapi.modules.auth.domain.model.RefreshToken;

public interface RefreshTokenRepository {
  RefreshToken save (RefreshToken refreshToken);

  Optional<RefreshToken> findByToken (String token);

  List<RefreshToken> findByUserId (Long userId);

}
