package com.hoaug.movieapi.modules.movie.application.usecase;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.movie.infrastructure.persistence.repository.JpaMoviePersonRepository;

@Component
public class DeleteMoviePersonUseCase {

  private final JpaMoviePersonRepository jpaMoviePersonRepository;

  public DeleteMoviePersonUseCase(JpaMoviePersonRepository jpaMoviePersonRepository) {
    this.jpaMoviePersonRepository = jpaMoviePersonRepository;
  }

  public void execute (Long id) {
    jpaMoviePersonRepository.deleteById(id);
  }
}
