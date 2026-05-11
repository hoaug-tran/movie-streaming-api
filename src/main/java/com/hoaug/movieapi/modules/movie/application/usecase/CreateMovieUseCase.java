package com.hoaug.movieapi.modules.movie.application.usecase;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.movie.application.dto.request.CreateMovieRequest;
import com.hoaug.movieapi.modules.movie.application.dto.response.MovieDetailResponse;
import com.hoaug.movieapi.modules.movie.application.mapper.MovieMapper;
import com.hoaug.movieapi.modules.movie.domain.model.Movie;
import com.hoaug.movieapi.modules.movie.domain.model.MovieStatus;
import com.hoaug.movieapi.modules.movie.domain.repository.MovieRepository;

@Component
public class CreateMovieUseCase {

  private final MovieRepository movieRepository;
  private final MovieMapper movieMapper;

  public CreateMovieUseCase(MovieRepository movieRepository, MovieMapper movieMapper) {
    this.movieRepository = movieRepository;
    this.movieMapper = movieMapper;
  }

  @CacheEvict(cacheNames = "movies", key = "'all_published_movies'")
  public MovieDetailResponse execute (CreateMovieRequest request) {
    if (movieRepository.existsBySlug(request.getSlug())) {
      throw new AppException(ErrorCode.MOVIE_SLUG_EXISTED);
    }

    Movie movie = new Movie();
    movie.setTitle(request.getTitle());
    movie.setOriginalTitle(request.getOriginalTitle());
    movie.setSlug(request.getSlug());
    movie.setDescription(request.getDescription());
    movie.setPosterUrl(request.getPosterUrl());
    movie.setBannerUrl(request.getBannerUrl());
    movie.setTrailerUrl(request.getTrailerUrl());
    movie.setReleaseYear(request.getReleaseYear());
    movie.setCountry(request.getCountry());
    movie.setLanguage(request.getLanguage());
    movie.setAgeRating(request.getAgeRating());
    movie.setMovieType(request.getMovieType());
    movie.setMovieStatus(request.getMovieStatus());
    movie.setIsPremiumOnly(request.getIsPremiumOnly());
    movie.setViewCount(0L);
    movie.setFavoriteCount(0L);
    movie.setAverageRating(BigDecimal.ZERO);
    movie.setTotalRatings(0);
    movie.setTotalReviews(0);
    movie.setCommentsLocked(false);
    movie.setReviewsLocked(false);
    movie.setCreatedAt(LocalDateTime.now());
    movie.setUpdatedAt(LocalDateTime.now());

    if (request.getMovieStatus() == MovieStatus.PUBLISHED) {
      movie.setPublishedAt(LocalDateTime.now());
    }

    Movie savedMovie = movieRepository.save(movie);
    return movieMapper.toDetailResponse(savedMovie, List.of(), List.of(), List.of(), List.of(),
        List.of());
  }
}