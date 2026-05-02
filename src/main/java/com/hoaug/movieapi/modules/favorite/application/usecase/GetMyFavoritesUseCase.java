package com.hoaug.movieapi.modules.favorite.application.usecase;

import java.util.List;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.favorite.application.dto.response.FavoriteListResponse;
import com.hoaug.movieapi.modules.favorite.application.dto.response.FavoriteResponse;
import com.hoaug.movieapi.modules.favorite.application.mapper.FavoriteMapper;
import com.hoaug.movieapi.modules.favorite.domain.model.Favorite;
import com.hoaug.movieapi.modules.favorite.domain.repository.FavoriteRepository;
import com.hoaug.movieapi.modules.movie.application.mapper.MovieMapper;
import com.hoaug.movieapi.modules.movie.domain.repository.MovieRepository;

@Component
public class GetMyFavoritesUseCase {

  private final FavoriteRepository favoriteRepository;
  private final FavoriteMapper favoriteMapper;
  private final MovieRepository movieRepository;
  private final MovieMapper movieMapper;

  public GetMyFavoritesUseCase(FavoriteRepository favoriteRepository,
      FavoriteMapper favoriteMapper, MovieRepository movieRepository, MovieMapper movieMapper) {
    this.favoriteRepository = favoriteRepository;
    this.favoriteMapper = favoriteMapper;
    this.movieRepository = movieRepository;
    this.movieMapper = movieMapper;
  }

  public FavoriteListResponse execute (Long userId) {
    List<FavoriteResponse> items = favoriteRepository.findByUserIdOrderByAddedAtDesc(userId).stream()
        .map(this::toResponse).toList();
    return new FavoriteListResponse(items);
  }

  private FavoriteResponse toResponse (Favorite favorite) {
    return favoriteMapper.toResponse(favorite, movieRepository.findById(favorite.getMovieId())
        .map(movieMapper::toBasicResponse).orElse(null));
  }
}