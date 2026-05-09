package com.hoaug.movieapi.common.config;

import java.time.Duration;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hoaug.movieapi.modules.auth.domain.model.AuthOtpChallenge;

@Configuration
@EnableCaching
public class CacheConfig {
  @Bean
  public CacheManager cacheManager (RedisConnectionFactory factory) {
    RedisCacheConfiguration defaultConfig = createCacheConfiguration(Duration.ofHours(1));

    return RedisCacheManager.builder(factory).cacheDefaults(defaultConfig)
        .withCacheConfiguration("movies", createCacheConfiguration(Duration.ofHours(24)))
        .withCacheConfiguration("searchResults", createCacheConfiguration(Duration.ofHours(1)))
        .withCacheConfiguration("recommendations", createCacheConfiguration(Duration.ofHours(4)))
        .withCacheConfiguration("movieDetail", createCacheConfiguration(Duration.ofHours(12)))
        .withCacheConfiguration("categories", createCacheConfiguration(Duration.ofHours(6)))
        .withCacheConfiguration("tags", createCacheConfiguration(Duration.ofHours(6))).build();
  }

  @Bean
  public RedisTemplate<String, AuthOtpChallenge> authOtpChallengeRedisTemplate (RedisConnectionFactory factory,
      ObjectMapper objectMapper) {
    RedisTemplate<String, AuthOtpChallenge> template = new RedisTemplate<>();
    Jackson2JsonRedisSerializer<AuthOtpChallenge> serializer = new Jackson2JsonRedisSerializer<>(objectMapper,
        AuthOtpChallenge.class);
    template.setConnectionFactory(factory);
    template.setKeySerializer(new StringRedisSerializer());
    template.setValueSerializer(serializer);
    template.setHashKeySerializer(new StringRedisSerializer());
    template.setHashValueSerializer(serializer);
    template.afterPropertiesSet();
    return template;
  }

  private RedisCacheConfiguration createCacheConfiguration (Duration ttl) {
    var serializer = org.springframework.data.redis.serializer.RedisSerializer.json();

    return RedisCacheConfiguration.defaultCacheConfig().entryTtl(ttl)
        .serializeKeysWith(
            RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
        .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer))
        .disableCachingNullValues();
  }
}