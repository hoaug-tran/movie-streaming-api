package com.hoaug.movieapi.modules.movie.application.usecase;

import java.time.LocalDateTime;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.movie.application.dto.response.MovieListResponse;
import com.hoaug.movieapi.modules.movie.application.mapper.MovieMapper;
import com.hoaug.movieapi.modules.movie.domain.model.Movie;
import com.hoaug.movieapi.modules.movie.domain.repository.MovieRepository;

@Component
public class GetWeeklyTrendingMoviesUseCase {

  private final MovieRepository movieRepository;
  private final MovieMapper movieMapper;
  private final GetMovieCategoriesUseCase getMovieCategoriesUseCase;

  public GetWeeklyTrendingMoviesUseCase(MovieRepository movieRepository, MovieMapper movieMapper,
      GetMovieCategoriesUseCase getMovieCategoriesUseCase) {
    this.movieRepository = movieRepository;
    this.movieMapper = movieMapper;
    this.getMovieCategoriesUseCase = getMovieCategoriesUseCase;
  }

  @Cacheable(value = "movies", key = "'weekly-trending:' + #limit")
  public MovieListResponse execute (int limit) {
    LocalDateTime since = LocalDateTime.now().minusDays(7);
    var movies = movieRepository.findTopTrendingThisWeek(since, limit);

    if (movies.size() < Math.min(limit, 4)) {
      var fallback = movieRepository.findTopTrending(limit);
      var seen = movies.stream().map(Movie::getId).collect(java.util.stream.Collectors.toSet());
      fallback.stream().filter(movie -> seen.add(movie.getId())).forEach(movies::add);
    }

    return MovieListResponse.builder()
        .movies(movies.stream().limit(limit)
            .map(movie -> movieMapper.toSummaryResponse(movie, getMovieCategoriesUseCase.execute(movie.getId())))
            .toList())
        .build();
  }
}
