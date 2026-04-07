package com.hoaug.movieapi.modules.movie.application.mapper;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.movie.application.dto.response.MovieStudioResponse;
import com.hoaug.movieapi.modules.movie.domain.model.MovieStudio;
import com.hoaug.movieapi.modules.movie.domain.model.Studio;

@Component
public class MovieStudioMapper {

  private final StudioMapper studioMapper;

  public MovieStudioMapper(StudioMapper studioMapper) {
    this.studioMapper = studioMapper;
  }

  public MovieStudioResponse toResponse (MovieStudio movieStudio, Studio studio) {
    MovieStudioResponse response = new MovieStudioResponse();
    response.setId(movieStudio.getId());
    response.setRole(movieStudio.getRole().name());
    if (studio != null) {
      response.setStudio(studioMapper.toResponse(studio));
    }
    return response;
  }
}
