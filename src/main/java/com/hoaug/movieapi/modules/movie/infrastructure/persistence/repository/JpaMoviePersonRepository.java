package com.hoaug.movieapi.modules.movie.infrastructure.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hoaug.movieapi.modules.movie.infrastructure.persistence.entity.MoviePersonEntity;

public interface JpaMoviePersonRepository extends JpaRepository<MoviePersonEntity, Long> {
  List<MoviePersonEntity> findByMovieIdOrderByDisplayOrderAsc (Long movieId);

  void deleteByMovieIdAndPersonId (Long movieId, Long personId);
}
