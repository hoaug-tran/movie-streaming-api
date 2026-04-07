package com.hoaug.movieapi.modules.movie.domain.repository;

import java.util.List;

import com.hoaug.movieapi.modules.movie.domain.model.Tag;

public interface TagRepository {
  List<Tag> findAll ();

  Tag findById (Long id);
}
