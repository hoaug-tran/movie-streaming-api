package com.hoaug.movieapi.modules.movie.application.usecase;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.movie.application.dto.request.UpdateMovieInteractionLocksRequest;
import com.hoaug.movieapi.modules.movie.application.dto.response.MovieDetailResponse;
import com.hoaug.movieapi.modules.movie.application.mapper.MovieMapper;
import com.hoaug.movieapi.modules.movie.domain.model.Movie;
import com.hoaug.movieapi.modules.movie.domain.repository.EpisodeRepository;
import com.hoaug.movieapi.modules.movie.domain.repository.MovieRepository;

@Component
public class UpdateMovieInteractionLocksUseCase {

  private final MovieRepository movieRepository;
  private final EpisodeRepository episodeRepository;
  private final GetMovieCategoriesUseCase getMovieCategoriesUseCase;
  private final GetMovieTagsUseCase getMovieTagsUseCase;
  private final GetMoviePersonsUseCase getMoviePersonsUseCase;
  private final GetMovieStudiosUseCase getMovieStudiosUseCase;
  private final MovieMapper movieMapper;

  public UpdateMovieInteractionLocksUseCase(MovieRepository movieRepository,
      EpisodeRepository episodeRepository, GetMovieCategoriesUseCase getMovieCategoriesUseCase,
      GetMovieTagsUseCase getMovieTagsUseCase, GetMoviePersonsUseCase getMoviePersonsUseCase,
      GetMovieStudiosUseCase getMovieStudiosUseCase, MovieMapper movieMapper) {
    this.movieRepository = movieRepository;
    this.episodeRepository = episodeRepository;
    this.getMovieCategoriesUseCase = getMovieCategoriesUseCase;
    this.getMovieTagsUseCase = getMovieTagsUseCase;
    this.getMoviePersonsUseCase = getMoviePersonsUseCase;
    this.getMovieStudiosUseCase = getMovieStudiosUseCase;
    this.movieMapper = movieMapper;
  }

  @CacheEvict(cacheNames = "movies", allEntries = true)
  public MovieDetailResponse execute (Long movieId, UpdateMovieInteractionLocksRequest request) {
    Movie movie = movieRepository.findById(movieId)
        .orElseThrow( () -> new AppException(ErrorCode.MOVIE_NOT_FOUND));

    movie.setCommentsLocked(request.getCommentsLocked());
    movie.setReviewsLocked(request.getReviewsLocked());

    Movie saved = movieRepository.save(movie);

    return movieMapper.toDetailResponse(saved, episodeRepository.findPublishedByMovieId(movieId),
        getMovieCategoriesUseCase.execute(movieId), getMovieTagsUseCase.execute(movieId),
        getMoviePersonsUseCase.execute(movieId), getMovieStudiosUseCase.execute(movieId));
  }
}
