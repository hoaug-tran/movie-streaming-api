package com.hoaug.movieapi.modules.movie.application.usecase;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.movie.application.dto.request.CreateStudioRequest;
import com.hoaug.movieapi.modules.movie.application.dto.response.StudioResponse;
import com.hoaug.movieapi.modules.movie.application.mapper.StudioMapper;
import com.hoaug.movieapi.modules.movie.domain.model.Studio;
import com.hoaug.movieapi.modules.movie.infrastructure.persistence.entity.StudioEntity;
import com.hoaug.movieapi.modules.movie.infrastructure.persistence.repository.JpaStudioRepository;

@Component
public class CreateStudioUseCase {

  private final JpaStudioRepository jpaStudioRepository;
  private final StudioMapper studioMapper;

  public CreateStudioUseCase(JpaStudioRepository jpaStudioRepository, StudioMapper studioMapper) {
    this.jpaStudioRepository = jpaStudioRepository;
    this.studioMapper = studioMapper;
  }

  public StudioResponse execute (CreateStudioRequest request) {
    StudioEntity entity = new StudioEntity();
    entity.setName(request.getName());
    entity.setSlug(request.getSlug());
    entity.setDescription(request.getDescription());
    entity.setLogoUrl(request.getLogoUrl());
    entity.setCountry(request.getCountry());
    entity.setWebsiteUrl(request.getWebsiteUrl());
    entity.setCreatedAt(LocalDateTime.now());
    entity.setUpdatedAt(LocalDateTime.now());

    var savedEntity = jpaStudioRepository.save(entity);

    Studio studio = new Studio();
    studio.setId(savedEntity.getId());
    studio.setName(savedEntity.getName());
    studio.setSlug(savedEntity.getSlug());
    studio.setDescription(savedEntity.getDescription());
    studio.setLogoUrl(savedEntity.getLogoUrl());
    studio.setCountry(savedEntity.getCountry());
    studio.setWebsiteUrl(savedEntity.getWebsiteUrl());

    return studioMapper.toResponse(studio);
  }
}
