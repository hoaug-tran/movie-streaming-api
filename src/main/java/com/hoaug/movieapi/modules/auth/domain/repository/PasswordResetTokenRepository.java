package com.hoaug.movieapi.modules.auth.domain.repository;

import java.util.Optional;

import com.hoaug.movieapi.modules.auth.domain.model.PasswordResetToken;

public interface PasswordResetTokenRepository {
  PasswordResetToken save (PasswordResetToken token);

  Optional<PasswordResetToken> findByToken (String token);
}
