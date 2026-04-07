package com.hoaug.movieapi.modules.movie.application.usecase;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.movie.application.dto.response.MovieDetailResponse;
import com.hoaug.movieapi.modules.movie.application.mapper.MovieMapper;
import com.hoaug.movieapi.modules.movie.domain.model.Movie;
import com.hoaug.movieapi.modules.movie.domain.repository.EpisodeRepository;
import com.hoaug.movieapi.modules.movie.domain.repository.MovieRepository;

@Component
public class GetMovieByIdUseCase {

  private final MovieRepository movieRepository;
  private final EpisodeRepository episodeRepository;
  private final MovieMapper movieMapper;
  private final GetMovieCategoriesUseCase getMovieCategoriesUseCase;
  private final GetMovieTagsUseCase getMovieTagsUseCase;
  private final GetMoviePersonsUseCase getMoviePersonsUseCase;
  private final GetMovieStudiosUseCase getMovieStudiosUseCase;

  public GetMovieByIdUseCase(MovieRepository movieRepository, EpisodeRepository episodeRepository,
      MovieMapper movieMapper, GetMovieCategoriesUseCase getMovieCategoriesUseCase,
      GetMovieTagsUseCase getMovieTagsUseCase, GetMoviePersonsUseCase getMoviePersonsUseCase,
      GetMovieStudiosUseCase getMovieStudiosUseCase) {
    this.movieRepository = movieRepository;
    this.episodeRepository = episodeRepository;
    this.movieMapper = movieMapper;
    this.getMovieCategoriesUseCase = getMovieCategoriesUseCase;
    this.getMovieTagsUseCase = getMovieTagsUseCase;
    this.getMoviePersonsUseCase = getMoviePersonsUseCase;
    this.getMovieStudiosUseCase = getMovieStudiosUseCase;
  }

  public MovieDetailResponse execute (Long movieId) {
    Movie movie = movieRepository.findPublishedById(movieId)
        .orElseThrow( () -> new AppException(ErrorCode.MOVIE_NOT_FOUND));

    return movieMapper.toDetailResponse(movie, episodeRepository.findPublishedByMovieId(movieId),
        getMovieCategoriesUseCase.execute(movieId), getMovieTagsUseCase.execute(movieId),
        getMoviePersonsUseCase.execute(movieId), getMovieStudiosUseCase.execute(movieId));
  }
}