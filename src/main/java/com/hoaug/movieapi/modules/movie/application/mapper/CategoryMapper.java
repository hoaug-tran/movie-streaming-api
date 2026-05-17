package com.hoaug.movieapi.modules.movie.application.mapper;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.movie.application.dto.response.CategoryResponse;
import com.hoaug.movieapi.modules.movie.domain.model.Category;

@Component
public class CategoryMapper {

  public CategoryResponse toResponse (Category category) {
    CategoryResponse response = new CategoryResponse();
    response.setId(category.getId());
    response.setName(category.getName());
    response.setSlug(category.getSlug());
    response.setDescription(category.getDescription());
    response.setCreatedAt(category.getCreatedAt());
    return response;
  }
}
