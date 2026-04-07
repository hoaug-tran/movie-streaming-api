package com.hoaug.movieapi.modules.movie.domain.repository;

import java.util.List;

import com.hoaug.movieapi.modules.movie.domain.model.Category;

public interface CategoryRepository {
  List<Category> findAll ();

  Category findById (Long id);
}
