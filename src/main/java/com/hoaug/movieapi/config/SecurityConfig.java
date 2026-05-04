package com.hoaug.movieapi.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.hoaug.movieapi.modules.auth.infrastructure.security.JwtAuthenticationFilter;

@Configuration(proxyBeanMethods = false)
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

  private final JwtAuthenticationFilter jwtAuthenticationFilter;
  private final List<String> allowedOrigins;

  public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter,
      @Value("${app.cors.allowed-origins:http://localhost:3000}") List<String> allowedOrigins) {
    this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    this.allowedOrigins = allowedOrigins;
  }

  @Bean
  public SecurityFilterChain filterChain (HttpSecurity http) throws Exception {
    http.cors(cors -> {
    }).csrf(csrf -> csrf.disable())
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth
            // Public endpoints - no auth needed
            .requestMatchers("/api/v1/auth/me").authenticated().requestMatchers("/api/v1/auth/**")
            .permitAll().requestMatchers(HttpMethod.GET, "/api/v1/movies/**").permitAll()
            .requestMatchers(HttpMethod.POST, "/api/v1/movies/search", "/api/v1/movies/search/advanced").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/v1/movies/categories").permitAll()
            .requestMatchers("/api/v1/subscription-plans/**").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/v1/subscriptions/plans").permitAll()
            .requestMatchers("/api/v1/payments/success").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/v1/advertisements/active", "/api/v1/advertisements/type/**").permitAll()
            .requestMatchers("/api/v1/ads/**").permitAll()
            .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
            .requestMatchers("/api/v1/webhooks/**").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/v1/search-histories/search").permitAll()
            .requestMatchers("/api/v1/discovery/**").permitAll()
            .requestMatchers("/actuator/health").permitAll()
            .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**")
            .permitAll()

            // Admin endpoints
            .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")

            // User personalization - auth required
            .requestMatchers("/api/v1/users/**").authenticated()
            .requestMatchers("/api/v1/watch-histories/**").authenticated()
            .requestMatchers("/api/v1/watchlists/**").authenticated()
            .requestMatchers("/api/v1/favorites/**").authenticated()
            .requestMatchers("/api/v1/comments/**").authenticated()
            .requestMatchers("/api/v1/reviews/**").authenticated()
            .requestMatchers("/api/v1/payments/**").authenticated()
            .requestMatchers("/api/v1/subscriptions/**").authenticated()
            .requestMatchers("/api/v1/notifications/**").authenticated()
            .requestMatchers("/api/v1/device-sessions/**").authenticated()
            .requestMatchers("/api/v1/recommendations/**").authenticated()
            .requestMatchers(HttpMethod.GET, "/api/v1/stream/keys/**").permitAll()
            .requestMatchers("/api/v1/search-histories/**").authenticated()

            // Default deny
            .anyRequest().authenticated())
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource () {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(allowedOrigins);
    configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    configuration.setAllowedHeaders(List.of("*"));
    configuration.setExposedHeaders(List.of("Authorization", "Content-Type"));
    configuration.setAllowCredentials(true);
    configuration.setMaxAge(3600L);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

  @Bean
  public PasswordEncoder passwordEncoder () {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager (AuthenticationConfiguration configuration)
      throws Exception {
    return configuration.getAuthenticationManager();
  }
}