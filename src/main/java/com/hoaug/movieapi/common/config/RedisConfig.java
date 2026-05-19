package com.hoaug.movieapi.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import tools.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import tools.jackson.databind.jsontype.PolymorphicTypeValidator;

@Configuration
public class RedisConfig {

  @Bean
  public PolymorphicTypeValidator redisTypeValidator () {
    return BasicPolymorphicTypeValidator.builder()
        .allowIfBaseType(Object.class)
        .build();
  }

  @Bean
  public GenericJacksonJsonRedisSerializer redisJsonSerializer (PolymorphicTypeValidator validator) {
    return GenericJacksonJsonRedisSerializer.builder()
        .enableDefaultTyping(validator)
        .enableSpringCacheNullValueSupport()
        .build();
  }

  @Bean
  public RedisTemplate<String, Object> redisTemplate (RedisConnectionFactory factory,
      GenericJacksonJsonRedisSerializer redisJsonSerializer) {
    RedisTemplate<String, Object> template = new RedisTemplate<>();
    template.setConnectionFactory(factory);
    template.setKeySerializer(new StringRedisSerializer());
    template.setValueSerializer(redisJsonSerializer);
    template.setHashKeySerializer(new StringRedisSerializer());
    template.setHashValueSerializer(redisJsonSerializer);
    template.afterPropertiesSet();
    return template;
  }
}
