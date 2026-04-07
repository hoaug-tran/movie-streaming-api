package com.hoaug.movieapi.modules.movie.application.usecase;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.movie.application.dto.response.StudioResponse;
import com.hoaug.movieapi.modules.movie.application.mapper.StudioMapper;
import com.hoaug.movieapi.modules.movie.domain.repository.StudioRepository;

@Component
public class GetStudioByIdUseCase {

  private final StudioRepository studioRepository;
  private final StudioMapper studioMapper;

  public GetStudioByIdUseCase(StudioRepository studioRepository, StudioMapper studioMapper) {
    this.studioRepository = studioRepository;
    this.studioMapper = studioMapper;
  }

  public StudioResponse execute (Long id) {
    var studio = studioRepository.findById(id);
    if (studio == null) {
      return null;
    }
    return studioMapper.toResponse(studio);
  }
}
