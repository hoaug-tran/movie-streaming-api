package com.hoaug.movieapi.modules.auth.domain.repository;

import java.util.Optional;

import com.hoaug.movieapi.modules.user.domain.model.User;

public interface AuthUserRepository {
  Optional<User> findById (Long id);

  Optional<User> findByUsername (String username);

  Optional<User> findByEmail (String email);

  boolean existsByUsername (String username);

  boolean existsByEmail (String email);

  User save (User user);
}
