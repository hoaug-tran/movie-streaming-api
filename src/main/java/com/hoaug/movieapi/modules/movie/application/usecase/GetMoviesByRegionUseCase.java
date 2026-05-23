package com.hoaug.movieapi.modules.movie.application.usecase;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.movie.application.dto.response.MovieListResponse;
import com.hoaug.movieapi.modules.movie.application.mapper.MovieMapper;
import com.hoaug.movieapi.modules.movie.domain.repository.MovieRepository;

@Component
public class GetMoviesByRegionUseCase {

  private final MovieRepository movieRepository;
  private final MovieMapper movieMapper;
  private final GetMovieCategoriesUseCase getMovieCategoriesUseCase;

  public GetMoviesByRegionUseCase(MovieRepository movieRepository, MovieMapper movieMapper, GetMovieCategoriesUseCase getMovieCategoriesUseCase) {
    this.movieRepository = movieRepository;
    this.movieMapper = movieMapper;
    this.getMovieCategoriesUseCase = getMovieCategoriesUseCase;
  }

  @Cacheable(value = "movies", key = "'region:' + #country + ':' + #page + ':' + #size")
  public MovieListResponse execute (String country, int page, int size) {
    List<com.hoaug.movieapi.modules.movie.domain.model.Movie> movies = "OTHER".equalsIgnoreCase(country)
        ? movieRepository.findRandomOtherCountries(
            List.of("south korea", "china", "united states", "vietnam"), size)
        : movieRepository.findByCountry(country, page, size);
    return MovieListResponse.builder()
        .movies(movies.stream()
            .map(movie -> movieMapper.toSummaryResponse(movie, getMovieCategoriesUseCase.execute(movie.getId()))).toList())
        .build();
  }
}
