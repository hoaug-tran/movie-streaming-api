package com.hoaug.movieapi.common.config;

import java.time.Duration;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

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
        .withCacheConfiguration("userSubscription",
            createCacheConfiguration(Duration.ofMinutes(15)))
        .withCacheConfiguration("movieDetail", createCacheConfiguration(Duration.ofHours(12)))
        .withCacheConfiguration("favorites", createCacheConfiguration(Duration.ofMinutes(5)))
        .withCacheConfiguration("watchlist", createCacheConfiguration(Duration.ofMinutes(5)))
        .withCacheConfiguration("watchHistory", createCacheConfiguration(Duration.ofMinutes(2)))
        .build();
  }

  private RedisCacheConfiguration createCacheConfiguration (Duration ttl) {
    return RedisCacheConfiguration.defaultCacheConfig().entryTtl(ttl)
        .serializeKeysWith(
            RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
        .serializeValuesWith(
            RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.json()))
        .disableCachingNullValues();
  }
}