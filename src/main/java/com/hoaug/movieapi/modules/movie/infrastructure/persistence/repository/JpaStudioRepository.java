package com.hoaug.movieapi.modules.movie.infrastructure.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hoaug.movieapi.modules.movie.infrastructure.persistence.entity.StudioEntity;

public interface JpaStudioRepository extends JpaRepository<StudioEntity, Long> {
  Optional<StudioEntity> findBySlug (String slug);

  boolean existsByNameIgnoreCase (String name);

  boolean existsBySlug (String slug);
}
