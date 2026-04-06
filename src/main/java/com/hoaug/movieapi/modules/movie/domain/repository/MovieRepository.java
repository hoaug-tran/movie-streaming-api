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
}
