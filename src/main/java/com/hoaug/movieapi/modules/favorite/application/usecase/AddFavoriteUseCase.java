package com.hoaug.movieapi.modules.favorite.application.usecase;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.event.EventPublisher;
import com.hoaug.movieapi.common.event.FavoriteAddedEvent;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.favorite.application.dto.response.FavoriteResponse;
import com.hoaug.movieapi.modules.favorite.application.mapper.FavoriteMapper;
import com.hoaug.movieapi.modules.favorite.domain.model.Favorite;
import com.hoaug.movieapi.modules.favorite.domain.repository.FavoriteRepository;
import com.hoaug.movieapi.modules.movie.domain.repository.MovieRepository;

@Component
public class AddFavoriteUseCase {

  private final FavoriteRepository favoriteRepository;
  private final MovieRepository movieRepository;
  private final FavoriteMapper favoriteMapper;
  private final EventPublisher eventPublisher;

  public AddFavoriteUseCase(FavoriteRepository favoriteRepository, MovieRepository movieRepository,
      FavoriteMapper favoriteMapper, EventPublisher eventPublisher) {
    this.favoriteRepository = favoriteRepository;
    this.movieRepository = movieRepository;
    this.favoriteMapper = favoriteMapper;
    this.eventPublisher = eventPublisher;
  }

  public FavoriteResponse execute (Long userId, Long movieId) {
    movieRepository.findById(movieId)
        .orElseThrow( () -> new AppException(ErrorCode.MOVIE_NOT_FOUND));

    boolean isNew = !favoriteRepository.findByUserIdAndMovieId(userId, movieId).isPresent();

    Favorite favorite = favoriteRepository.findByUserIdAndMovieId(userId, movieId)
        .orElseGet( () -> {
          Favorite item = new Favorite();
          item.setUserId(userId);
          item.setMovieId(movieId);
          item.setAddedAt(LocalDateTime.now());
          return item;
        });

    Favorite saved = favoriteRepository.save(favorite);

    if (isNew) {
      eventPublisher.publish(new FavoriteAddedEvent(userId, movieId));
    }

    return favoriteMapper.toResponse(saved);
  }
}