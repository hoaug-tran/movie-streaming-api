package com.hoaug.movieapi.modules.movie.domain.repository;

import java.util.List;
import java.util.Optional;

import com.hoaug.movieapi.modules.movie.domain.model.Studio;

public interface StudioRepository {
  List<Studio> findAll ();

  Optional<Studio> findById (Long id);

  Studio save (Studio studio);

  void deleteById (Long id);
}
