package com.hoaug.movieapi.modules.movie.application.usecase;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.movie.infrastructure.persistence.repository.JpaMovieTagRepository;

@Component
public class DeleteMovieTagUseCase {

  private final JpaMovieTagRepository jpaMovieTagRepository;

  public DeleteMovieTagUseCase(JpaMovieTagRepository jpaMovieTagRepository) {
    this.jpaMovieTagRepository = jpaMovieTagRepository;
  }

  public void execute (Long movieId, Long tagId) {
    jpaMovieTagRepository.deleteByMovieIdAndTagId(movieId, tagId);
  }
}
