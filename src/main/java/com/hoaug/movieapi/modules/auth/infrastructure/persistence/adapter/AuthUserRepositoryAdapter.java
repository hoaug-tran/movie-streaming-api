package com.hoaug.movieapi.modules.auth.infrastructure.persistence.adapter;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.auth.domain.repository.AuthUserRepository;
import com.hoaug.movieapi.modules.user.domain.model.User;
import com.hoaug.movieapi.modules.user.domain.repository.UserRepository;

@Component
public class AuthUserRepositoryAdapter implements AuthUserRepository {
  private final UserRepository userRepository;

  public AuthUserRepositoryAdapter(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public Optional<User> findById (Long id) {
    return userRepository.findById(id);
  }

  @Override
  public Optional<User> findByUsername (String username) {
    return userRepository.findByUsername(username);
  }

  @Override
  public Optional<User> findByEmail (String email) {
    return userRepository.findByEmail(email);
  }

  @Override
  public boolean existsByUsername (String username) {
    return userRepository.existsByUsername(username);
  }

  @Override
  public boolean existsByEmail (String email) {
    return userRepository.existsByEmail(email);
  }

  @Override
  public User save (User user) {
    return userRepository.save(user);
  }
}
