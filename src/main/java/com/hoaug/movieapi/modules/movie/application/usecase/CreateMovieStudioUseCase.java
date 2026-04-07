package com.hoaug.movieapi.modules.movie.application.usecase;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.movie.application.dto.request.CreateMovieStudioRequest;
import com.hoaug.movieapi.modules.movie.application.dto.response.MovieStudioResponse;
import com.hoaug.movieapi.modules.movie.application.mapper.MovieStudioMapper;
import com.hoaug.movieapi.modules.movie.domain.model.MovieStudio;
import com.hoaug.movieapi.modules.movie.domain.model.MovieStudioRole;
import com.hoaug.movieapi.modules.movie.domain.repository.StudioRepository;
import com.hoaug.movieapi.modules.movie.infrastructure.persistence.entity.MovieStudioEntity;
import com.hoaug.movieapi.modules.movie.infrastructure.persistence.repository.JpaMovieStudioRepository;

@Component
public class CreateMovieStudioUseCase {

  private final JpaMovieStudioRepository jpaMovieStudioRepository;
  private final StudioRepository studioRepository;
  private final MovieStudioMapper movieStudioMapper;

  public CreateMovieStudioUseCase(JpaMovieStudioRepository jpaMovieStudioRepository,
      StudioRepository studioRepository, MovieStudioMapper movieStudioMapper) {
    this.jpaMovieStudioRepository = jpaMovieStudioRepository;
    this.studioRepository = studioRepository;
    this.movieStudioMapper = movieStudioMapper;
  }

  public MovieStudioResponse execute (Long movieId, CreateMovieStudioRequest request) {
    MovieStudioEntity entity = new MovieStudioEntity();
    entity.setMovieId(movieId);
    entity.setStudioId(request.getStudioId());
    entity.setRole(MovieStudioRole.valueOf(request.getRole()));
    entity.setCreatedAt(LocalDateTime.now());

    var savedEntity = jpaMovieStudioRepository.save(entity);

    MovieStudio movieStudio = new MovieStudio();
    movieStudio.setId(savedEntity.getId());
    movieStudio.setMovieId(savedEntity.getMovieId());
    movieStudio.setStudioId(savedEntity.getStudioId());
    movieStudio.setRole(savedEntity.getRole());
    movieStudio.setCreatedAt(savedEntity.getCreatedAt());

    var studio = studioRepository.findById(savedEntity.getStudioId());
    return movieStudioMapper.toResponse(movieStudio, studio);
  }
}
