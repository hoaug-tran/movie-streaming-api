package com.hoaug.movieapi.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = { "com.hoaug.movieapi.modules.*.domain.repository" })
public class PersistenceConfig {
}
