package com.hoaug.movieapi.modules.movie.infrastructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hoaug.movieapi.modules.movie.infrastructure.persistence.entity.TagEntity;

public interface JpaTagRepository extends JpaRepository<TagEntity, Long> {
  boolean existsByNameIgnoreCase (String name);

  boolean existsBySlug (String slug);
}