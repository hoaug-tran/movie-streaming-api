package com.hoaug.movieapi.modules.movie.application.mapper;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.movie.application.dto.response.StudioResponse;
import com.hoaug.movieapi.modules.movie.domain.model.Studio;

@Component
public class StudioMapper {

  public StudioResponse toResponse (Studio studio) {
    StudioResponse response = new StudioResponse();
    response.setId(studio.getId());
    response.setName(studio.getName());
    response.setSlug(studio.getSlug());
    response.setDescription(studio.getDescription());
    response.setLogoUrl(studio.getLogoUrl());
    response.setCountry(studio.getCountry());
    response.setWebsiteUrl(studio.getWebsiteUrl());
    return response;
  }
}
