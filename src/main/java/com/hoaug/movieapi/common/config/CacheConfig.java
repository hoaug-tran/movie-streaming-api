package com.hoaug.movieapi.common.config;

import java.time.Duration;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import tools.jackson.databind.JavaType;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

import com.hoaug.movieapi.modules.auth.domain.model.AuthOtpChallenge;
import com.hoaug.movieapi.modules.subscription.application.dto.response.SubscriptionPlanResponse;

@Configuration
@EnableCaching
public class CacheConfig implements CachingConfigurer {

  private static final Logger log = LoggerFactory.getLogger(CacheConfig.class);

  public static final String CACHE_MOVIES_LIST = "movies";
  public static final String CACHE_MOVIE_DETAIL_BY_ID = "movieDetail";
  public static final String CACHE_MOVIE_DETAIL_BY_SLUG = "movieDetailBySlug";
  public static final String CACHE_MOVIE_SEARCH = "searchResults";
  public static final String CACHE_RECOMMENDATIONS = "recommendations";
  public static final String CACHE_CATEGORIES = "categories";
  public static final String CACHE_TAGS = "tags";
  public static final String CACHE_SUBSCRIPTION_PLANS = "subscriptionPlans";

  @Bean
  public CacheManager cacheManager (RedisConnectionFactory factory,
      GenericJacksonJsonRedisSerializer redisJsonSerializer) {
    RedisCacheConfiguration defaultConfig = createCacheConfiguration(Duration.ofHours(1),
        redisJsonSerializer);

    return RedisCacheManager.builder(factory).cacheDefaults(defaultConfig)
        .withCacheConfiguration(CACHE_MOVIES_LIST,
            createCacheConfiguration(Duration.ofHours(6), redisJsonSerializer))
        .withCacheConfiguration(CACHE_MOVIE_DETAIL_BY_ID,
            createCacheConfiguration(Duration.ofHours(12), redisJsonSerializer))
        .withCacheConfiguration(CACHE_MOVIE_DETAIL_BY_SLUG,
            createCacheConfiguration(Duration.ofHours(12), redisJsonSerializer))
        .withCacheConfiguration(CACHE_MOVIE_SEARCH,
            createCacheConfiguration(Duration.ofMinutes(30), redisJsonSerializer))
        .withCacheConfiguration(CACHE_RECOMMENDATIONS,
            createCacheConfiguration(Duration.ofHours(4), redisJsonSerializer))
        .withCacheConfiguration(CACHE_CATEGORIES,
            createCacheConfiguration(Duration.ofHours(6), redisJsonSerializer))
        .withCacheConfiguration(CACHE_TAGS,
            createCacheConfiguration(Duration.ofHours(6), redisJsonSerializer))
        .withCacheConfiguration(CACHE_SUBSCRIPTION_PLANS,
            createTypedListCacheConfiguration(Duration.ofHours(12), SubscriptionPlanResponse.class))
        .transactionAware().build();
  }

  @Bean
  @Profile("local")
  public CommandLineRunner flushStaleCacheOnStartup (RedisConnectionFactory factory) {
    return args -> {
      try (var connection = factory.getConnection()) {
        connection.serverCommands().flushDb();
        log.info("[CacheConfig] Flushed Redis DB on local profile startup to clear stale cache entries");
      } catch (Exception ex) {
        log.warn("[CacheConfig] Could not flush Redis on startup: {}", ex.getMessage());
      }
    };
  }

  @Bean
  public RedisTemplate<String, AuthOtpChallenge> authOtpChallengeRedisTemplate (
      RedisConnectionFactory factory) {
    RedisTemplate<String, AuthOtpChallenge> template = new RedisTemplate<>();
    JacksonJsonRedisSerializer<AuthOtpChallenge> serializer = new JacksonJsonRedisSerializer<>(
        AuthOtpChallenge.class);
    template.setConnectionFactory(factory);
    template.setKeySerializer(new StringRedisSerializer());
    template.setValueSerializer(serializer);
    template.setHashKeySerializer(new StringRedisSerializer());
    template.setHashValueSerializer(serializer);
    template.afterPropertiesSet();
    return template;
  }

  @Override
  public CacheErrorHandler errorHandler () {
    return new CacheErrorHandler() {
      @Override
      public void handleCacheGetError (RuntimeException exception, Cache cache, Object key) {
        log.warn("Redis cache get failed [cache={}, key={}]: {}. Falling back to source.",
            cache.getName(), key, exception.getMessage());
        evictQuietly(cache, key);
      }

      @Override
      public void handleCachePutError (RuntimeException exception, Cache cache, Object key,
          Object value) {
        log.warn("Redis cache put failed [cache={}, key={}]: {}", cache.getName(), key,
            exception.getMessage());
      }

      @Override
      public void handleCacheEvictError (RuntimeException exception, Cache cache, Object key) {
        log.warn("Redis cache evict failed [cache={}, key={}]: {}", cache.getName(), key,
            exception.getMessage());
      }

      @Override
      public void handleCacheClearError (RuntimeException exception, Cache cache) {
        log.warn("Redis cache clear failed [cache={}]: {}", cache.getName(),
            exception.getMessage());
      }

      private void evictQuietly (Cache cache, Object key) {
        try {
          cache.evict(key);
        } catch (RuntimeException ignored) {
          // shutup
        }
      }
    };
  }

  private RedisCacheConfiguration createCacheConfiguration (Duration ttl,
      GenericJacksonJsonRedisSerializer serializer) {
    return RedisCacheConfiguration.defaultCacheConfig().entryTtl(ttl)
        .serializeKeysWith(
            RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
        .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer))
        .disableCachingNullValues();
  }

  private RedisCacheConfiguration createTypedListCacheConfiguration (Duration ttl,
      Class<?> elementType) {
    ObjectMapper mapper = JsonMapper.builder().build();
    JavaType listType = mapper.getTypeFactory().constructCollectionType(List.class, elementType);
    JacksonJsonRedisSerializer<List<?>> serializer = new JacksonJsonRedisSerializer<>(mapper, listType);
    return RedisCacheConfiguration.defaultCacheConfig().entryTtl(ttl)
        .serializeKeysWith(
            RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
        .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer))
        .disableCachingNullValues();
  }
}