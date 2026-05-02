package com.hoaug.movieapi.modules.favorite.application.mapper;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.favorite.application.dto.response.FavoriteResponse;
import com.hoaug.movieapi.modules.favorite.domain.model.Favorite;
import com.hoaug.movieapi.modules.movie.application.dto.response.MovieBasicResponse;

@Component
public class FavoriteMapper {

  public FavoriteResponse toResponse (Favorite favorite) {
    return toResponse(favorite, null);
  }

  public FavoriteResponse toResponse (Favorite favorite, MovieBasicResponse movie) {
    FavoriteResponse response = new FavoriteResponse();
    response.setId(favorite.getId());
    response.setMovieId(favorite.getMovieId());
    response.setAddedAt(favorite.getAddedAt());
    response.setMovie(movie);
    return response;
  }
}