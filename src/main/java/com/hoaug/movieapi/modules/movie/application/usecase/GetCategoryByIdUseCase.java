package com.hoaug.movieapi.modules.movie.application.usecase;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.movie.application.dto.response.CategoryResponse;
import com.hoaug.movieapi.modules.movie.application.mapper.CategoryMapper;
import com.hoaug.movieapi.modules.movie.domain.model.Category;
import com.hoaug.movieapi.modules.movie.domain.repository.CategoryRepository;

@Component
public class GetCategoryByIdUseCase {

  private final CategoryRepository categoryRepository;
  private final CategoryMapper categoryMapper;

  public GetCategoryByIdUseCase(CategoryRepository categoryRepository,
      CategoryMapper categoryMapper) {
    this.categoryRepository = categoryRepository;
    this.categoryMapper = categoryMapper;
  }

  public CategoryResponse execute (Long id) {
    Category category = categoryRepository.findById(id)
        .orElseThrow( () -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
    return categoryMapper.toResponse(category);
  }
}
