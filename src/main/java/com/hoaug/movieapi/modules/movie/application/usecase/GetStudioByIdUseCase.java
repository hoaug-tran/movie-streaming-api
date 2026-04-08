package com.hoaug.movieapi.modules.movie.application.usecase;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
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
    var studio = studioRepository.findById(id)
        .orElseThrow( () -> new AppException(ErrorCode.STUDIO_NOT_FOUND));
    return studioMapper.toResponse(studio);
  }
}
