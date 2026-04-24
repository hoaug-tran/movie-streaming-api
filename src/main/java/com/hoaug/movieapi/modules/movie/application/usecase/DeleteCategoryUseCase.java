package com.hoaug.movieapi.modules.movie.application.usecase;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.movie.domain.repository.CategoryRepository;

@Component
public class DeleteCategoryUseCase {

  private final CategoryRepository categoryRepository;

  public DeleteCategoryUseCase(CategoryRepository categoryRepository) {
    this.categoryRepository = categoryRepository;
  }

  @CacheEvict(cacheNames = "categories", allEntries = true)
  public void execute (Long id) {
    categoryRepository.findById(id)
        .orElseThrow( () -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
    categoryRepository.deleteById(id);
  }
}
