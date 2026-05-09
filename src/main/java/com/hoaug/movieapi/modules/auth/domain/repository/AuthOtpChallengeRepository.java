package com.hoaug.movieapi.modules.auth.domain.repository;

import java.time.Duration;
import java.util.Optional;

import com.hoaug.movieapi.modules.auth.domain.model.AuthOtpChallenge;

public interface AuthOtpChallengeRepository {
  AuthOtpChallenge save (AuthOtpChallenge challenge, Duration ttl);

  Optional<AuthOtpChallenge> findByPurposeAndChallengeToken (String purpose, String challengeToken);

  void deleteByPurposeAndChallengeToken (String purpose, String challengeToken);
}
