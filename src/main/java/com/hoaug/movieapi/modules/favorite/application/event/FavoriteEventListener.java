package com.hoaug.movieapi.modules.favorite.application.event;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.event.FavoriteAddedEvent;
import com.hoaug.movieapi.common.event.FavoriteRemovedEvent;
import com.hoaug.movieapi.modules.movie.domain.repository.MovieRepository;

@Component
public class FavoriteEventListener {
  private final MovieRepository movieRepository;

  public FavoriteEventListener(MovieRepository movieRepository) {
    this.movieRepository = movieRepository;
  }

  @EventListener
  public void onFavoriteAdded (FavoriteAddedEvent event) {
    var movie = movieRepository.findById(event.getMovieId()).orElse(null);
    if (movie != null) {
      movie.setFavoriteCount(movie.getFavoriteCount() + 1);
      movieRepository.save(movie);
    }
  }

  @EventListener
  public void onFavoriteRemoved (FavoriteRemovedEvent event) {
    var movie = movieRepository.findById(event.getMovieId()).orElse(null);
    if (movie != null && movie.getFavoriteCount() > 0) {
      movie.setFavoriteCount(movie.getFavoriteCount() - 1);
      movieRepository.save(movie);
    }
  }
}
