package com.hoaug.movieapi.modules.watchlist.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.watchlist.domain.model.Watchlist;
import com.hoaug.movieapi.modules.watchlist.domain.repository.WatchlistRepository;
import com.hoaug.movieapi.modules.watchlist.infrastructure.persistence.entity.WatchlistEntity;
import com.hoaug.movieapi.modules.watchlist.infrastructure.persistence.repository.JpaWatchlistRepository;

@Component
public class WatchlistRepositoryAdapter implements WatchlistRepository {

  private final JpaWatchlistRepository jpaWatchlistRepository;

  public WatchlistRepositoryAdapter(JpaWatchlistRepository jpaWatchlistRepository) {
    this.jpaWatchlistRepository = jpaWatchlistRepository;
  }

  @Override
  public Optional<Watchlist> findByUserIdAndMovieId (Long userId, Long movieId) {
    return jpaWatchlistRepository.findByUserIdAndMovieId(userId, movieId).map(this::toDomain);
  }

  @Override
  public List<Watchlist> findByUserIdOrderByAddedAtDesc (Long userId) {
    return jpaWatchlistRepository.findByUserIdOrderByAddedAtDesc(userId).stream()
        .map(this::toDomain).toList();
  }

  @Override
  public Watchlist save (Watchlist watchlist) {
    WatchlistEntity savedEntity = jpaWatchlistRepository.save(toEntity(watchlist));
    return toDomain(savedEntity);
  }

  @Override
  public void delete (Watchlist watchlist) {
    jpaWatchlistRepository.delete(toEntity(watchlist));
  }

  private Watchlist toDomain (WatchlistEntity entity) {
    Watchlist watchlist = new Watchlist();
    watchlist.setId(entity.getId());
    watchlist.setUserId(entity.getUserId());
    watchlist.setMovieId(entity.getMovieId());
    watchlist.setAddedAt(entity.getAddedAt());
    return watchlist;
  }

  private WatchlistEntity toEntity (Watchlist watchlist) {
    WatchlistEntity entity = new WatchlistEntity();
    entity.setId(watchlist.getId());
    entity.setUserId(watchlist.getUserId());
    entity.setMovieId(watchlist.getMovieId());
    entity.setAddedAt(watchlist.getAddedAt());
    return entity;
  }
}