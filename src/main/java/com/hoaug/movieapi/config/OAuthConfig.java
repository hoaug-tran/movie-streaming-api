package com.hoaug.movieapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrations;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;

import com.hoaug.movieapi.config.properties.OAuthProperties;

@Configuration
public class OAuthConfig {
  private final OAuthProperties oAuthProperties;

  public OAuthConfig(OAuthProperties oAuthProperties) {
    this.oAuthProperties = oAuthProperties;
  }

  @Bean
  public InMemoryClientRegistrationRepository clientRegistrationRepository () {
    return new InMemoryClientRegistrationRepository(googleClientRegistration());
  }

  private ClientRegistration googleClientRegistration () {
    return ClientRegistrations.fromIssuerLocation("https://accounts.google.com")
        .clientId(oAuthProperties.getGoogle().getClientId())
        .clientSecret(oAuthProperties.getGoogle().getClientSecret())
        .redirectUri(oAuthProperties.getGoogle().getRedirectUri() + "/oauth2/code/google")
        .scope(oAuthProperties.getGoogle().getScopes().split(" ")).registrationId("google").build();
  }
}
