package com.hoaug.movieapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.hoaug.movieapi.modules")
public class MovieStreamingApiApplication {

  public static void main (String[] args) {
    SpringApplication.run(MovieStreamingApiApplication.class, args);
  }
}
