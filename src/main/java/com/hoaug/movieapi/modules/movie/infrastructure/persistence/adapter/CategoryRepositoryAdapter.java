package com.hoaug.movieapi.modules.movie.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.movie.domain.model.Category;
import com.hoaug.movieapi.modules.movie.domain.repository.CategoryRepository;
import com.hoaug.movieapi.modules.movie.infrastructure.persistence.entity.CategoryEntity;
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
      category.setCreatedAt(entity.getCreatedAt());
      return category;
    }).toList();
  }

  @Override
  public Optional<Category> findById (Long id) {
    return jpaCategoryRepository.findById(id).map(entity -> {
      Category category = new Category();
      category.setId(entity.getId());
      category.setName(entity.getName());
      category.setSlug(entity.getSlug());
      category.setDescription(entity.getDescription());
      category.setCreatedAt(entity.getCreatedAt());
      return category;
    });
  }

  @Override
  public Category save (Category category) {
    CategoryEntity entity = new CategoryEntity();
    entity.setId(category.getId());
    entity.setName(category.getName());
    entity.setSlug(category.getSlug());
    entity.setDescription(category.getDescription());

    CategoryEntity savedEntity = jpaCategoryRepository.save(entity);

    Category result = new Category();
    result.setId(savedEntity.getId());
    result.setName(savedEntity.getName());
    result.setSlug(savedEntity.getSlug());
    result.setDescription(savedEntity.getDescription());
    result.setCreatedAt(savedEntity.getCreatedAt());
    return result;
  }

  @Override
  public void deleteById (Long id) {
    jpaCategoryRepository.deleteById(id);
  }
}