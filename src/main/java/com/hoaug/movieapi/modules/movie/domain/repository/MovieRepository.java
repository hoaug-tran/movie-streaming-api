package com.hoaug.movieapi.modules.movie.domain.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.hoaug.movieapi.modules.movie.domain.model.Movie;

public interface MovieRepository {
  List<Movie> findAllPublished ();

  List<Movie> findAll ();

  Optional<Movie> findById (Long id);

  Optional<Movie> findPublishedById (Long id);

  Optional<Movie> findPublishedBySlug (String slug);

  boolean existsBySlug (String slug);

  Movie save (Movie movie);

  void deleteById (Long id);

  List<Movie> findTopTrending (int limit);

  List<Movie> findTopTrendingThisWeek (LocalDateTime since, int limit);

  List<Movie> findWeeklyNew (int limit);

  List<Movie> findTopRated (int limit);

  List<Movie> findTopSeries (int limit);

  List<Movie> findUpcoming (int limit);

  List<Movie> findByCountry (String country, int page, int size);

  List<Movie> findRandomOtherCountries (List<String> excludedCountries, int limit);

  List<Movie> findTopSeriesByCountry (String country, int limit);

  List<Movie> findTopSeriesDrama (int limit);

  List<Movie> findActionMovies (int limit);

  List<Movie> findThrillerMovies (int limit);

  List<Movie> findAnimeSeries (int limit);

  List<Movie> findAnimeMovies (int limit);

  List<Movie> findMostCommented (int limit);

  void incrementViewCount (Long movieId);
}
