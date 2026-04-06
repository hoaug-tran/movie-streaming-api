package com.hoaug.movieapi.modules.user.domain.repository;

import java.util.List;
import java.util.Optional;

import com.hoaug.movieapi.modules.user.domain.model.User;

public interface UserRepository {
  Optional<User> findById (Long id);

  Optional<User> findByUsername (String username);

  Optional<User> findByEmail (String email);

  boolean existsByUsername (String username);

  boolean existsByEmail (String email);

  List<User> findAll ();

  User save (User user);

  void deleteById (Long id);
}
