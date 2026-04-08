package com.hoaug.movieapi.modules.movie.application.usecase;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.movie.application.dto.request.UpdateStudioRequest;
import com.hoaug.movieapi.modules.movie.application.dto.response.StudioResponse;
import com.hoaug.movieapi.modules.movie.application.mapper.StudioMapper;
import com.hoaug.movieapi.modules.movie.domain.model.Studio;
import com.hoaug.movieapi.modules.movie.domain.repository.StudioRepository;

@Component
public class UpdateStudioUseCase {

  private final StudioRepository studioRepository;
  private final StudioMapper studioMapper;

  public UpdateStudioUseCase(StudioRepository studioRepository, StudioMapper studioMapper) {
    this.studioRepository = studioRepository;
    this.studioMapper = studioMapper;
  }

  public StudioResponse execute (Long id, UpdateStudioRequest request) {
    Studio studio = studioRepository.findById(id)
        .orElseThrow( () -> new AppException(ErrorCode.STUDIO_NOT_FOUND));

    studio.setName(request.getName());
    studio.setSlug(request.getSlug());
    studio.setDescription(request.getDescription());
    studio.setLogoUrl(request.getLogoUrl());
    studio.setCountry(request.getCountry());
    studio.setWebsiteUrl(request.getWebsiteUrl());

    Studio saved = studioRepository.save(studio);
    return studioMapper.toResponse(saved);
  }
}
