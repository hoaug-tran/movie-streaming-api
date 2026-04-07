package com.hoaug.movieapi.modules.movie.application.usecase;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.movie.infrastructure.persistence.repository.JpaMovieCategoryRepository;

@Component
public class DeleteMovieCategoryUseCase {

  private final JpaMovieCategoryRepository jpaMovieCategoryRepository;

  public DeleteMovieCategoryUseCase(JpaMovieCategoryRepository jpaMovieCategoryRepository) {
    this.jpaMovieCategoryRepository = jpaMovieCategoryRepository;
  }

  public void execute (Long movieId, Long categoryId) {
    jpaMovieCategoryRepository.deleteByMovieIdAndCategoryId(movieId, categoryId);
  }
}
