package com.hoaug.movieapi.modules.movie.application.mapper;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.movie.application.dto.response.TagResponse;
import com.hoaug.movieapi.modules.movie.domain.model.Tag;

@Component
public class TagMapper {

  public TagResponse toResponse (Tag tag) {
    TagResponse response = new TagResponse();
    response.setId(tag.getId());
    response.setName(tag.getName());
    response.setSlug(tag.getSlug());
    response.setDescription(tag.getDescription());
    return response;
  }
}
