package com.hoaug.movieapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.hoaug.movieapi.modules")
@EnableAsync
@EnableScheduling
public class MovieStreamingApiApplication {

  public static void main (String[] args) {
    SpringApplication.run(MovieStreamingApiApplication.class, args);
  }
}
