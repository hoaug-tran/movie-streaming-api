package com.hoaug.movieapi.modules.movie.domain.repository;

import java.util.List;

import com.hoaug.movieapi.modules.movie.domain.model.Studio;

public interface StudioRepository {
  List<Studio> findAll ();

  Studio findById (Long id);

  Studio save (Studio studio);

  void delete (Long id);
}
