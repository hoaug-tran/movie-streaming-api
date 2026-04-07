package com.hoaug.movieapi.modules.movie.application.usecase;

import java.util.List;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.movie.application.dto.response.StudioResponse;
import com.hoaug.movieapi.modules.movie.application.mapper.StudioMapper;
import com.hoaug.movieapi.modules.movie.domain.repository.StudioRepository;

@Component
public class GetStudiosUseCase {

  private final StudioRepository studioRepository;
  private final StudioMapper studioMapper;

  public GetStudiosUseCase(StudioRepository studioRepository, StudioMapper studioMapper) {
    this.studioRepository = studioRepository;
    this.studioMapper = studioMapper;
  }

  public List<StudioResponse> execute () {
    return studioRepository.findAll().stream().map(studioMapper::toResponse).toList();
  }
}
