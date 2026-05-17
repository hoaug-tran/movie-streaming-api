package com.hoaug.movieapi.modules.movie.application.usecase;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.movie.application.dto.request.CreateMovieStudioRequest;
import com.hoaug.movieapi.modules.movie.application.dto.response.MovieStudioResponse;
import com.hoaug.movieapi.modules.movie.application.mapper.MovieStudioMapper;
import com.hoaug.movieapi.modules.movie.domain.model.MovieStudio;
import com.hoaug.movieapi.modules.movie.domain.model.MovieStudioRole;
import com.hoaug.movieapi.modules.movie.domain.repository.StudioRepository;
import com.hoaug.movieapi.modules.movie.infrastructure.persistence.repository.JpaMovieStudioRepository;

@Component
public class UpdateMovieStudioUseCase {

  private final JpaMovieStudioRepository jpaMovieStudioRepository;
  private final StudioRepository studioRepository;
  private final MovieStudioMapper movieStudioMapper;

  public UpdateMovieStudioUseCase(JpaMovieStudioRepository jpaMovieStudioRepository,
      StudioRepository studioRepository, MovieStudioMapper movieStudioMapper) {
    this.jpaMovieStudioRepository = jpaMovieStudioRepository;
    this.studioRepository = studioRepository;
    this.movieStudioMapper = movieStudioMapper;
  }

  public MovieStudioResponse execute(Long movieStudioId, CreateMovieStudioRequest request) {
    var entity = jpaMovieStudioRepository.findById(movieStudioId)
        .orElseThrow(() -> new IllegalArgumentException("MovieStudio not found: " + movieStudioId));

    if (request.getRole() != null) {
      entity.setRole(MovieStudioRole.valueOf(request.getRole()));
    }

    var saved = jpaMovieStudioRepository.save(entity);

    MovieStudio movieStudio = new MovieStudio();
    movieStudio.setId(saved.getId());
    movieStudio.setMovieId(saved.getMovieId());
    movieStudio.setStudioId(saved.getStudioId());
    movieStudio.setRole(saved.getRole());
    movieStudio.setCreatedAt(saved.getCreatedAt());

    var studio = studioRepository.findById(saved.getStudioId()).get();
    return movieStudioMapper.toResponse(movieStudio, studio);
  }
}
