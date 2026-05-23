package com.hoaug.movieapi.modules.movie.domain.repository;

import java.util.List;
import java.util.Optional;

import com.hoaug.movieapi.modules.movie.domain.model.Tag;

public interface TagRepository {
  List<Tag> findAll ();

  Optional<Tag> findById (Long id);

  boolean existsByNameIgnoreCase (String name);

  boolean existsBySlug (String slug);

  Tag save (Tag tag);

  void deleteById (Long id);
}
