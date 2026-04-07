package com.hoaug.movieapi.modules.movie.application.usecase;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.movie.infrastructure.persistence.repository.JpaMovieStudioRepository;

@Component
public class DeleteMovieStudioUseCase {

  private final JpaMovieStudioRepository jpaMovieStudioRepository;

  public DeleteMovieStudioUseCase(JpaMovieStudioRepository jpaMovieStudioRepository) {
    this.jpaMovieStudioRepository = jpaMovieStudioRepository;
  }

  public void execute (Long id) {
    jpaMovieStudioRepository.deleteById(id);
  }
}
