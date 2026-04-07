package com.hoaug.movieapi.modules.movie.application.usecase;

import java.util.List;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.movie.application.dto.response.MovieStudioResponse;
import com.hoaug.movieapi.modules.movie.application.mapper.MovieStudioMapper;
import com.hoaug.movieapi.modules.movie.domain.model.MovieStudio;
import com.hoaug.movieapi.modules.movie.domain.repository.StudioRepository;
import com.hoaug.movieapi.modules.movie.infrastructure.persistence.repository.JpaMovieStudioRepository;

@Component
public class GetMovieStudiosUseCase {

  private final JpaMovieStudioRepository jpaMovieStudioRepository;
  private final StudioRepository studioRepository;
  private final MovieStudioMapper movieStudioMapper;

  public GetMovieStudiosUseCase(JpaMovieStudioRepository jpaMovieStudioRepository,
      StudioRepository studioRepository, MovieStudioMapper movieStudioMapper) {
    this.jpaMovieStudioRepository = jpaMovieStudioRepository;
    this.studioRepository = studioRepository;
    this.movieStudioMapper = movieStudioMapper;
  }

  public List<MovieStudioResponse> execute (Long movieId) {
    return jpaMovieStudioRepository.findByMovieId(movieId).stream().map(entity -> {
      MovieStudio movieStudio = new MovieStudio();
      movieStudio.setId(entity.getId());
      movieStudio.setMovieId(entity.getMovieId());
      movieStudio.setStudioId(entity.getStudioId());
      movieStudio.setRole(entity.getRole());
      movieStudio.setCreatedAt(entity.getCreatedAt());

      var studio = studioRepository.findById(entity.getStudioId());
      return movieStudioMapper.toResponse(movieStudio, studio);
    }).toList();
  }
}
