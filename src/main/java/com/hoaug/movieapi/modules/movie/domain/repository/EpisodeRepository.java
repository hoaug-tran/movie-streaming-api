package com.hoaug.movieapi.modules.movie.domain.repository;

import java.util.List;
import java.util.Optional;

import com.hoaug.movieapi.modules.movie.domain.model.Episode;

public interface EpisodeRepository {
  List<Episode> findPublishedByMovieId (Long movieId);

  List<Episode> findAllByMovieId (Long movieId);

  Optional<Episode> findById (Long id);

  Episode save (Episode episode);

  void deleteById (Long id);
}
