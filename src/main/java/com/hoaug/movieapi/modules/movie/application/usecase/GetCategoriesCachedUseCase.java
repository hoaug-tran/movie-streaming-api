package com.hoaug.movieapi.modules.movie.application.usecase;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.movie.application.dto.response.CategoryListResponse;
import com.hoaug.movieapi.modules.movie.application.dto.response.CategoryResponse;
import com.hoaug.movieapi.modules.movie.application.mapper.CategoryMapper;
import com.hoaug.movieapi.modules.movie.domain.repository.CategoryRepository;

@Component
public class GetCategoriesCachedUseCase {
  private final CategoryRepository categoryRepository;
  private final CategoryMapper categoryMapper;

  public GetCategoriesCachedUseCase(CategoryRepository categoryRepository,
      CategoryMapper categoryMapper) {
    this.categoryRepository = categoryRepository;
    this.categoryMapper = categoryMapper;
  }

  @Cacheable(value = "categories", unless = "#result == null")
  public CategoryListResponse execute () {
    List<CategoryResponse> categories = categoryRepository.findAll().stream()
        .map(categoryMapper::toResponse)
        .toList();
    return new CategoryListResponse(categories);
  }
}
