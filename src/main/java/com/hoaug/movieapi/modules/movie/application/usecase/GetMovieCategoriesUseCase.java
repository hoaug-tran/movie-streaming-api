package com.hoaug.movieapi.modules.movie.application.usecase;

import java.util.List;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.movie.application.dto.response.CategoryResponse;
import com.hoaug.movieapi.modules.movie.application.mapper.CategoryMapper;
import com.hoaug.movieapi.modules.movie.domain.repository.CategoryRepository;
import com.hoaug.movieapi.modules.movie.infrastructure.persistence.repository.JpaMovieCategoryRepository;

@Component
public class GetMovieCategoriesUseCase {

  private final JpaMovieCategoryRepository jpaMovieCategoryRepository;
  private final CategoryRepository categoryRepository;
  private final CategoryMapper categoryMapper;

  public GetMovieCategoriesUseCase(JpaMovieCategoryRepository jpaMovieCategoryRepository,
      CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
    this.jpaMovieCategoryRepository = jpaMovieCategoryRepository;
    this.categoryRepository = categoryRepository;
    this.categoryMapper = categoryMapper;
  }

  public List<CategoryResponse> execute (Long movieId) {
    return jpaMovieCategoryRepository.findByMovieId(movieId).stream().map(entity -> {
      var category = categoryRepository.findById(entity.getCategoryId());
      return categoryMapper.toResponse(category);
    }).toList();
  }
}
