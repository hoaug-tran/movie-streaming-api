package com.hoaug.movieapi.modules.movie.application.usecase;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.movie.application.dto.request.CreateCategoryRequest;
import com.hoaug.movieapi.modules.movie.application.dto.response.CategoryResponse;
import com.hoaug.movieapi.modules.movie.application.mapper.CategoryMapper;
import com.hoaug.movieapi.modules.movie.domain.model.Category;
import com.hoaug.movieapi.modules.movie.domain.repository.CategoryRepository;

@Component
public class CreateCategoryUseCase {

  private final CategoryRepository categoryRepository;
  private final CategoryMapper categoryMapper;

  public CreateCategoryUseCase(CategoryRepository categoryRepository,
      CategoryMapper categoryMapper) {
    this.categoryRepository = categoryRepository;
    this.categoryMapper = categoryMapper;
  }

  @CacheEvict(cacheNames = "categories", allEntries = true)
  public CategoryResponse execute (CreateCategoryRequest request) {
    if (categoryRepository.existsByNameIgnoreCase(request.getName())
        || categoryRepository.existsBySlug(request.getSlug())) {
      throw new AppException(ErrorCode.CATEGORY_EXISTED);
    }

    Category category = new Category();
    category.setName(request.getName());
    category.setSlug(request.getSlug());
    category.setDescription(request.getDescription());

    Category saved = categoryRepository.save(category);
    return categoryMapper.toResponse(saved);
  }
}
