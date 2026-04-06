package com.hoaug.movieapi.modules.user.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.user.domain.model.User;
import com.hoaug.movieapi.modules.user.domain.repository.UserRepository;
import com.hoaug.movieapi.modules.user.infrastructure.persistence.entity.UserEntity;
import com.hoaug.movieapi.modules.user.infrastructure.persistence.repository.JpaUserRepository;

@Component
public class UserRepositoryAdapter implements UserRepository {
  private final JpaUserRepository jpaUserRepository;

  public UserRepositoryAdapter(JpaUserRepository jpaUserRepository) {
    this.jpaUserRepository = jpaUserRepository;
  }

  @Override
  public Optional<User> findById (Long id) {
    return jpaUserRepository.findById(id).map(this::toDomain);
  }

  @Override
  public Optional<User> findByUsername (String username) {
    return jpaUserRepository.findByUsername(username).map(this::toDomain);
  }

  @Override
  public Optional<User> findByEmail (String email) {
    return jpaUserRepository.findByEmail(email).map(this::toDomain);
  }

  @Override
  public boolean existsByUsername (String username) {
    return jpaUserRepository.existsByUsername(username);
  }

  @Override
  public boolean existsByEmail (String email) {
    return jpaUserRepository.existsByEmail(email);
  }

  @Override
  public List<User> findAll () {
    return jpaUserRepository.findAll().stream().map(this::toDomain).toList();
  }

  @Override
  public User save (User user) {
    UserEntity entity = toEntity(user);
    UserEntity savedEntity = jpaUserRepository.save(entity);
    return toDomain(savedEntity);
  }

  private User toDomain (UserEntity entity) {
    User user = new User();
    user.setId(entity.getId());
    user.setUsername(entity.getUsername());
    user.setEmail(entity.getEmail());
    user.setPassword(entity.getPassword());
    user.setFullName(entity.getFullName());
    user.setAvatarUrl(entity.getAvatarUrl());
    user.setRole(entity.getRole());
    user.setAccountStatus(entity.getAccountStatus());
    user.setPremiumExpiryDate(entity.getPremiumExpiryDate());
    user.setCreatedAt(entity.getCreatedAt());
    user.setUpdatedAt(entity.getUpdatedAt());
    user.setLastLoginAt(entity.getLastLoginAt());
    return user;
  }

  private UserEntity toEntity (User user) {
    UserEntity entity = new UserEntity();
    entity.setId(user.getId());
    entity.setUsername(user.getUsername());
    entity.setEmail(user.getEmail());
    entity.setPassword(user.getPassword());
    entity.setFullName(user.getFullName());
    entity.setAvatarUrl(user.getAvatarUrl());
    entity.setRole(user.getRole());
    entity.setAccountStatus(user.getAccountStatus());
    entity.setPremiumExpiryDate(user.getPremiumExpiryDate());
    entity.setCreatedAt(user.getCreatedAt());
    entity.setUpdatedAt(user.getUpdatedAt());
    entity.setLastLoginAt(user.getLastLoginAt());
    return entity;
  }

  @Override
  public void deleteById (Long id) {
    jpaUserRepository.deleteById(id);
  }
}
