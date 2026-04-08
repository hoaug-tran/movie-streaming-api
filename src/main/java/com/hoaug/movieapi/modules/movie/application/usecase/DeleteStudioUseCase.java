package com.hoaug.movieapi.modules.movie.application.usecase;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.movie.domain.repository.StudioRepository;

@Component
public class DeleteStudioUseCase {

  private final StudioRepository studioRepository;

  public DeleteStudioUseCase(StudioRepository studioRepository) {
    this.studioRepository = studioRepository;
  }

  public void execute (Long id) {
    studioRepository.findById(id).orElseThrow( () -> new AppException(ErrorCode.STUDIO_NOT_FOUND));
    studioRepository.deleteById(id);
  }
}
