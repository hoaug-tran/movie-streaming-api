package com.hoaug.movieapi.modules.watchlist.domain.repository;

import java.util.List;
import java.util.Optional;

import com.hoaug.movieapi.modules.watchlist.domain.model.Watchlist;

public interface WatchlistRepository {
  Optional<Watchlist> findByUserIdAndMovieId (Long userId, Long movieId);

  List<Watchlist> findByUserIdOrderByAddedAtDesc (Long userId);

  Watchlist save (Watchlist watchlist);

  void delete (Watchlist watchlist);
}