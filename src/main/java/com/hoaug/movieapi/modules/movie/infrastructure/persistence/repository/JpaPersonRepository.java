package com.hoaug.movieapi.modules.movie.infrastructure.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hoaug.movieapi.modules.movie.infrastructure.persistence.entity.PersonEntity;

public interface JpaPersonRepository extends JpaRepository<PersonEntity, Long> {
  List<PersonEntity> findByFullNameContainingIgnoreCase (String fullName);
}
