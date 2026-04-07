package com.hoaug.movieapi.modules.movie.infrastructure.persistence.adapter;

import java.util.List;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.movie.domain.model.Category;
import com.hoaug.movieapi.modules.movie.domain.repository.CategoryRepository;
import com.hoaug.movieapi.modules.movie.infrastructure.persistence.repository.JpaCategoryRepository;

@Component
public class CategoryRepositoryAdapter implements CategoryRepository {

  private final JpaCategoryRepository jpaCategoryRepository;

  public CategoryRepositoryAdapter(JpaCategoryRepository jpaCategoryRepository) {
    this.jpaCategoryRepository = jpaCategoryRepository;
  }

  @Override
  public List<Category> findAll () {
    return jpaCategoryRepository.findAll().stream().map(entity -> {
      Category category = new Category();
      category.setId(entity.getId());
      category.setName(entity.getName());
      category.setSlug(entity.getSlug());
      category.setDescription(entity.getDescription());
      return category;
    }).toList();
  }

  @Override
  public Category findById (Long id) {
    return jpaCategoryRepository.findById(id).map(entity -> {
      Category category = new Category();
      category.setId(entity.getId());
      category.setName(entity.getName());
      category.setSlug(entity.getSlug());
      category.setDescription(entity.getDescription());
      return category;
    }).orElse(null);
  }
}