package com.hoaug.movieapi.modules.movie.domain.repository;

import java.util.List;
import java.util.Optional;

import com.hoaug.movieapi.modules.movie.domain.model.Category;

public interface CategoryRepository {
  List<Category> findAll ();

  Optional<Category> findById (Long id);

  boolean existsByNameIgnoreCase (String name);

  boolean existsBySlug (String slug);

  Category save (Category category);

  void deleteById (Long id);
}
