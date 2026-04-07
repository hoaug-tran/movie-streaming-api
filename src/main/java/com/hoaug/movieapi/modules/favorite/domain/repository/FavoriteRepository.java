package com.hoaug.movieapi.modules.favorite.domain.repository;

import java.util.List;
import java.util.Optional;

import com.hoaug.movieapi.modules.favorite.domain.model.Favorite;

public interface FavoriteRepository {
  Optional<Favorite> findByUserIdAndMovieId (Long userId, Long movieId);

  List<Favorite> findByUserIdOrderByAddedAtDesc (Long userId);

  Favorite save (Favorite favorite);

  void delete (Favorite favorite);
}