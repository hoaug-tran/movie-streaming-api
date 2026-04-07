package com.hoaug.movieapi.modules.movie.infrastructure.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hoaug.movieapi.modules.movie.infrastructure.persistence.entity.MovieStudioEntity;

public interface JpaMovieStudioRepository extends JpaRepository<MovieStudioEntity, Long> {
  List<MovieStudioEntity> findByMovieId (Long movieId);

  void deleteByMovieIdAndStudioId (Long movieId, Long studioId);
}
