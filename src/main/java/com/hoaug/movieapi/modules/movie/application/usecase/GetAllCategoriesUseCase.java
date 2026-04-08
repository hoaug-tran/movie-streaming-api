package com.hoaug.movieapi.modules.movie.application.usecase;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.movie.application.dto.request.CreateCategoryRequest;
import com.hoaug.movieapi.modules.movie.application.dto.response.CategoryResponse;
import com.hoaug.movieapi.modules.movie.application.mapper.CategoryMapper;
import com.hoaug.movieapi.modules.movie.domain.model.Category;
import com.hoaug.movieapi.modules.movie.domain.repository.CategoryRepository;

@Component
public class GetAllCategoriesUseCase {

  private final CategoryRepository categoryRepository;
  private final CategoryMapper categoryMapper;

  public GetAllCategoriesUseCase(CategoryRepository categoryRepository,
      CategoryMapper categoryMapper) {
    this.categoryRepository = categoryRepository;
    this.categoryMapper = categoryMapper;
  }

  public List<CategoryResponse> execute () {
    List<Category> categories = categoryRepository.findAll();
    return categories.stream().map(categoryMapper::toResponse).toList();
  }
}
