package com.hoaug.movieapi.modules.auth.infrastructure.persistence.adapter;

import java.time.Duration;
import java.util.Optional;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.auth.domain.model.AuthOtpChallenge;
import com.hoaug.movieapi.modules.auth.domain.repository.AuthOtpChallengeRepository;

@Component
public class RedisAuthOtpChallengeRepository implements AuthOtpChallengeRepository {
  private static final String KEY_PREFIX = "auth:otp:";

  private final RedisTemplate<String, Object> redisTemplate;

  public RedisAuthOtpChallengeRepository(RedisTemplate<String, Object> redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

  @Override
  public AuthOtpChallenge save (AuthOtpChallenge challenge, Duration ttl) {
    redisTemplate.opsForValue().set(key(challenge.getPurpose(), challenge.getChallengeToken()), challenge, ttl);
    return challenge;
  }

  @Override
  public Optional<AuthOtpChallenge> findByPurposeAndChallengeToken (String purpose, String challengeToken) {
    Object value = redisTemplate.opsForValue().get(key(purpose, challengeToken));
    return Optional.ofNullable(value != null ? (AuthOtpChallenge) value : null);
  }

  @Override
  public void deleteByPurposeAndChallengeToken (String purpose, String challengeToken) {
    redisTemplate.delete(key(purpose, challengeToken));
  }

  private String key (String purpose, String challengeToken) {
    return KEY_PREFIX + purpose + ":" + challengeToken;
  }
}
