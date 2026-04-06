package com.hoaug.movieapi.modules.movie.application.usecase;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.movie.application.dto.request.UpdateMovieRequest;
import com.hoaug.movieapi.modules.movie.application.dto.response.MovieDetailResponse;
import com.hoaug.movieapi.modules.movie.application.mapper.MovieMapper;
import com.hoaug.movieapi.modules.movie.domain.model.Movie;
import com.hoaug.movieapi.modules.movie.domain.model.MovieStatus;
import com.hoaug.movieapi.modules.movie.domain.repository.EpisodeRepository;
import com.hoaug.movieapi.modules.movie.domain.repository.MovieRepository;

@Component
public class UpdateMovieUseCase {

  private final MovieRepository movieRepository;
  private final EpisodeRepository episodeRepository;
  private final MovieMapper movieMapper;

  public UpdateMovieUseCase(MovieRepository movieRepository, EpisodeRepository episodeRepository,
      MovieMapper movieMapper) {
    this.movieRepository = movieRepository;
    this.episodeRepository = episodeRepository;
    this.movieMapper = movieMapper;
  }

  public MovieDetailResponse execute (Long movieId, UpdateMovieRequest request) {
    Movie movie = movieRepository.findById(movieId)
        .orElseThrow( () -> new AppException(ErrorCode.MOVIE_NOT_FOUND));

    movie.setTitle(request.getTitle());
    movie.setOriginalTitle(request.getOriginalTitle());
    movie.setDescription(request.getDescription());
    movie.setPosterUrl(request.getPosterUrl());
    movie.setBannerUrl(request.getBannerUrl());
    movie.setTrailerUrl(request.getTrailerUrl());
    movie.setReleaseYear(request.getReleaseYear());
    movie.setCountry(request.getCountry());
    movie.setLanguage(request.getLanguage());
    movie.setAgeRating(request.getAgeRating());
    movie.setMovieStatus(request.getMovieStatus());
    movie.setIsPremiumOnly(request.getIsPremiumOnly());
    movie.setUpdatedAt(LocalDateTime.now());

    if (request.getMovieStatus() == MovieStatus.PUBLISHED && movie.getPublishedAt() == null) {
      movie.setPublishedAt(LocalDateTime.now());
    }

    Movie savedMovie = movieRepository.save(movie);

    return movieMapper.toDetailResponse(savedMovie,
        episodeRepository.findPublishedByMovieId(savedMovie.getId()));
  }
}