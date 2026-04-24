package com.hoaug.movieapi.modules.movie.application.usecase;

import java.time.LocalDateTime;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.movie.application.dto.request.UpdateMovieStatusRequest;
import com.hoaug.movieapi.modules.movie.application.dto.response.MovieDetailResponse;
import com.hoaug.movieapi.modules.movie.application.mapper.MovieMapper;
import com.hoaug.movieapi.modules.movie.domain.model.Movie;
import com.hoaug.movieapi.modules.movie.domain.model.MovieStatus;
import com.hoaug.movieapi.modules.movie.domain.repository.EpisodeRepository;
import com.hoaug.movieapi.modules.movie.domain.repository.MovieRepository;

@Component
public class UpdateMovieStatusUseCase {

  private final MovieRepository movieRepository;
  private final EpisodeRepository episodeRepository;
  private final MovieMapper movieMapper;
  private final GetMovieCategoriesUseCase getMovieCategoriesUseCase;
  private final GetMovieTagsUseCase getMovieTagsUseCase;
  private final GetMoviePersonsUseCase getMoviePersonsUseCase;
  private final GetMovieStudiosUseCase getMovieStudiosUseCase;

  public UpdateMovieStatusUseCase(MovieRepository movieRepository,
      EpisodeRepository episodeRepository, MovieMapper movieMapper,
      GetMovieCategoriesUseCase getMovieCategoriesUseCase, GetMovieTagsUseCase getMovieTagsUseCase,
      GetMoviePersonsUseCase getMoviePersonsUseCase,
      GetMovieStudiosUseCase getMovieStudiosUseCase) {
    this.movieRepository = movieRepository;
    this.episodeRepository = episodeRepository;
    this.movieMapper = movieMapper;
    this.getMovieCategoriesUseCase = getMovieCategoriesUseCase;
    this.getMovieTagsUseCase = getMovieTagsUseCase;
    this.getMoviePersonsUseCase = getMoviePersonsUseCase;
    this.getMovieStudiosUseCase = getMovieStudiosUseCase;
  }

  @Caching(evict = {
      @CacheEvict(cacheNames = "movies", key = "'all_published_movies'"),
      @CacheEvict(cacheNames = "movieDetail", key = "#movieId")
  })
  public MovieDetailResponse execute (Long movieId, UpdateMovieStatusRequest request) {
    Movie movie = movieRepository.findById(movieId)
        .orElseThrow( () -> new AppException(ErrorCode.MOVIE_NOT_FOUND));

    movie.setMovieStatus(request.getStatus());
    if (request.getStatus() == MovieStatus.PUBLISHED && movie.getPublishedAt() == null) {
      movie.setPublishedAt(LocalDateTime.now());
    }

    Movie saved = movieRepository.save(movie);

    return movieMapper.toDetailResponse(saved, episodeRepository.findPublishedByMovieId(movieId),
        getMovieCategoriesUseCase.execute(movieId), getMovieTagsUseCase.execute(movieId),
        getMoviePersonsUseCase.execute(movieId), getMovieStudiosUseCase.execute(movieId));
  }
}
