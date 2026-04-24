package com.hoaug.movieapi.modules.movie.domain.repository;

import java.util.List;
import java.util.Optional;

import com.hoaug.movieapi.modules.movie.domain.model.Movie;

public interface MovieRepository {
  List<Movie> findAllPublished ();

  Optional<Movie> findById (Long id);

  Optional<Movie> findPublishedById (Long id);

  Optional<Movie> findPublishedBySlug (String slug);

  boolean existsBySlug (String slug);

  Movie save (Movie movie);

  void deleteById (Long id);

  List<Movie> findTopTrending (int limit);

  List<Movie> findWeeklyNew (int limit);

  List<Movie> findTopRated (int limit);

  List<Movie> findTopSeries (int limit);

  List<Movie> findUpcoming (int limit);

  List<Movie> findByCountry (String country, int page, int size);
}
