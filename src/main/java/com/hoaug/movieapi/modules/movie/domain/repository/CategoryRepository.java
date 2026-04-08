package com.hoaug.movieapi.modules.movie.domain.repository;

import java.util.List;
import java.util.Optional;

import com.hoaug.movieapi.modules.movie.domain.model.Category;

public interface CategoryRepository {
  List<Category> findAll ();

  Optional<Category> findById (Long id);

  Category save (Category category);

  void deleteById (Long id);
}
