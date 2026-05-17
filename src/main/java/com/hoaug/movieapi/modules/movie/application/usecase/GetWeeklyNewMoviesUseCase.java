package com.hoaug.movieapi.modules.movie.application.usecase;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.movie.application.dto.response.MovieListResponse;
import com.hoaug.movieapi.modules.movie.application.mapper.MovieMapper;
import com.hoaug.movieapi.modules.movie.domain.model.Movie;
import com.hoaug.movieapi.modules.movie.domain.repository.MovieRepository;

@Component
public class GetWeeklyNewMoviesUseCase {

  private static final Logger log = LoggerFactory.getLogger(GetWeeklyNewMoviesUseCase.class);

  private final MovieRepository movieRepository;
  private final MovieMapper movieMapper;
  private final GetMovieCategoriesUseCase getMovieCategoriesUseCase;

  public GetWeeklyNewMoviesUseCase(MovieRepository movieRepository, MovieMapper movieMapper, GetMovieCategoriesUseCase getMovieCategoriesUseCase) {
    this.movieRepository = movieRepository;
    this.movieMapper = movieMapper;
    this.getMovieCategoriesUseCase = getMovieCategoriesUseCase;
  }

  @Cacheable(value = "movies", key = "'weekly:' + #limit", unless = "#result.movies.isEmpty()")
  public MovieListResponse execute (int limit) {
    List<Movie> movies = movieRepository.findWeeklyNew(limit);
    log.info("[WeeklyNew] findWeeklyNew({}) returned {} movies", limit, movies.size());
    movies.forEach(m -> log.info("[WeeklyNew] movie id={} title={} status={} publishedAt={} createdAt={}",
        m.getId(), m.getTitle(), m.getMovieStatus(), m.getPublishedAt(), m.getCreatedAt()));
    return MovieListResponse.builder()
        .movies(movies.stream()
            .map(movie -> movieMapper.toSummaryResponse(movie, getMovieCategoriesUseCase.execute(movie.getId()))).toList())
        .build();
  }
}
