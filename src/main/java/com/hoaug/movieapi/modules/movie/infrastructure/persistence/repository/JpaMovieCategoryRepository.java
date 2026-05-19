package com.hoaug.movieapi.modules.movie.infrastructure.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hoaug.movieapi.modules.movie.infrastructure.persistence.entity.MovieCategoryEntity;
import com.hoaug.movieapi.modules.movie.infrastructure.persistence.entity.MovieCategoryEntity.MovieCategoryId;

public interface JpaMovieCategoryRepository
    extends JpaRepository<MovieCategoryEntity, MovieCategoryId> {
  List<MovieCategoryEntity> findByMovieId (Long movieId);

  List<MovieCategoryEntity> findByMovieIdIn (java.util.Collection<Long> movieIds);

  List<MovieCategoryEntity> findByCategoryIdIn (java.util.Collection<Long> categoryIds);

  void deleteByMovieIdAndCategoryId (Long movieId, Long categoryId);
}
