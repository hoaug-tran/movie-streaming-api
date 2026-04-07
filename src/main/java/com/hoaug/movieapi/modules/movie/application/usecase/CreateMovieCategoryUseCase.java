package com.hoaug.movieapi.modules.movie.application.usecase;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.movie.application.dto.request.CreateMovieCategoryRequest;
import com.hoaug.movieapi.modules.movie.application.dto.response.CategoryResponse;
import com.hoaug.movieapi.modules.movie.application.mapper.CategoryMapper;
import com.hoaug.movieapi.modules.movie.domain.repository.CategoryRepository;
import com.hoaug.movieapi.modules.movie.infrastructure.persistence.entity.MovieCategoryEntity;
import com.hoaug.movieapi.modules.movie.infrastructure.persistence.repository.JpaMovieCategoryRepository;

@Component
public class CreateMovieCategoryUseCase {

  private final JpaMovieCategoryRepository jpaMovieCategoryRepository;
  private final CategoryRepository categoryRepository;
  private final CategoryMapper categoryMapper;

  public CreateMovieCategoryUseCase(JpaMovieCategoryRepository jpaMovieCategoryRepository,
      CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
    this.jpaMovieCategoryRepository = jpaMovieCategoryRepository;
    this.categoryRepository = categoryRepository;
    this.categoryMapper = categoryMapper;
  }

  public CategoryResponse execute (Long movieId, CreateMovieCategoryRequest request) {
    MovieCategoryEntity entity = new MovieCategoryEntity();
    entity.setMovieId(movieId);
    entity.setCategoryId(request.getCategoryId());

    jpaMovieCategoryRepository.save(entity);

    var category = categoryRepository.findById(request.getCategoryId());
    return categoryMapper.toResponse(category);
  }
}
