package com.hoaug.movieapi.modules.movie.domain.repository;

import java.util.List;

import com.hoaug.movieapi.modules.movie.domain.model.Episode;

public interface EpisodeRepository {
  List<Episode> findPublishedByMovieId (Long movieId);

  Episode save (Episode episode);

  void deleteById (Long id);
}
