package com.hoaug.movieapi.modules.movie.infrastructure.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.hoaug.movieapi.modules.movie.domain.model.MovieStatus;
import com.hoaug.movieapi.modules.movie.infrastructure.persistence.entity.MovieEntity;

public interface JpaMovieRepository extends JpaRepository<MovieEntity, Long> {
  List<MovieEntity> findByMovieStatus (MovieStatus movieStatus);

  Optional<MovieEntity> findByIdAndMovieStatus (Long id, MovieStatus movieStatus);

  Optional<MovieEntity> findBySlugAndMovieStatus (String slug, MovieStatus movieStatus);

  boolean existsBySlug (String slug);

  Page<MovieEntity> findByMovieStatus (MovieStatus movieStatus, Pageable pageable);

  Page<MovieEntity> findByMovieStatusAndTitleContaining (MovieStatus movieStatus, String title,
      Pageable pageable);
}