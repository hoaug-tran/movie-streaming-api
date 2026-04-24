package com.hoaug.movieapi.modules.favorite.application.usecase;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.event.EventPublisher;
import com.hoaug.movieapi.common.event.FavoriteRemovedEvent;
import com.hoaug.movieapi.modules.favorite.domain.repository.FavoriteRepository;

@Component
public class RemoveFavoriteUseCase {

  private final FavoriteRepository favoriteRepository;
  private final EventPublisher eventPublisher;

  public RemoveFavoriteUseCase(FavoriteRepository favoriteRepository,
      EventPublisher eventPublisher) {
    this.favoriteRepository = favoriteRepository;
    this.eventPublisher = eventPublisher;
  }

  public void execute (Long userId, Long movieId) {
    favoriteRepository.findByUserIdAndMovieId(userId, movieId).ifPresent(favorite -> {
      favoriteRepository.delete(favorite);
      eventPublisher.publish(new FavoriteRemovedEvent(userId, movieId));
    });
  }
}