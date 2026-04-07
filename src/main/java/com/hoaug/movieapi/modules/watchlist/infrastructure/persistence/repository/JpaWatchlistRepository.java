package com.hoaug.movieapi.modules.watchlist.infrastructure.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hoaug.movieapi.modules.watchlist.infrastructure.persistence.entity.WatchlistEntity;

public interface JpaWatchlistRepository extends JpaRepository<WatchlistEntity, Long> {
  Optional<WatchlistEntity> findByUserIdAndMovieId (Long userId, Long movieId);

  List<WatchlistEntity> findByUserIdOrderByAddedAtDesc (Long userId);
}