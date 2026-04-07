package com.hoaug.movieapi.modules.movie.infrastructure.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hoaug.movieapi.modules.movie.infrastructure.persistence.entity.MovieTagEntity;
import com.hoaug.movieapi.modules.movie.infrastructure.persistence.entity.MovieTagEntity.MovieTagId;

public interface JpaMovieTagRepository extends JpaRepository<MovieTagEntity, MovieTagId> {
  List<MovieTagEntity> findByMovieId (Long movieId);

  void deleteByMovieIdAndTagId (Long movieId, Long tagId);
}
