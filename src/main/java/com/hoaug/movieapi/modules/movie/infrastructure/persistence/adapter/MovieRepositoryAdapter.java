package com.hoaug.movieapi.modules.movie.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.movie.domain.model.Movie;
import com.hoaug.movieapi.modules.movie.domain.model.MovieStatus;
import com.hoaug.movieapi.modules.movie.domain.repository.MovieRepository;
import com.hoaug.movieapi.modules.movie.infrastructure.persistence.entity.MovieEntity;
import com.hoaug.movieapi.modules.movie.infrastructure.persistence.repository.JpaMovieRepository;

@Component
public class MovieRepositoryAdapter implements MovieRepository {

  private final JpaMovieRepository jpaMovieRepository;

  public MovieRepositoryAdapter(JpaMovieRepository jpaMovieRepository) {
    this.jpaMovieRepository = jpaMovieRepository;
  }

  @Override
  public List<Movie> findAllPublished () {
    return jpaMovieRepository.findByMovieStatus(MovieStatus.PUBLISHED).stream().map(this::toDomain)
        .toList();
  }

  @Override
  public Optional<Movie> findById (Long id) {
    return jpaMovieRepository.findById(id).map(this::toDomain);
  }

  @Override
  public Optional<Movie> findPublishedById (Long id) {
    return jpaMovieRepository.findByIdAndMovieStatus(id, MovieStatus.PUBLISHED).map(this::toDomain);
  }

  @Override
  public Optional<Movie> findPublishedBySlug (String slug) {
    return jpaMovieRepository.findBySlugAndMovieStatus(slug, MovieStatus.PUBLISHED)
        .map(this::toDomain);
  }

  @Override
  public boolean existsBySlug (String slug) {
    return jpaMovieRepository.existsBySlug(slug);
  }

  @Override
  public Movie save (Movie movie) {
    MovieEntity savedEntity = jpaMovieRepository.save(toEntity(movie));
    return toDomain(savedEntity);
  }

  private Movie toDomain (MovieEntity entity) {
    Movie movie = new Movie();
    movie.setId(entity.getId());
    movie.setTitle(entity.getTitle());
    movie.setOriginalTitle(entity.getOriginalTitle());
    movie.setSlug(entity.getSlug());
    movie.setDescription(entity.getDescription());
    movie.setPosterUrl(entity.getPosterUrl());
    movie.setBannerUrl(entity.getBannerUrl());
    movie.setTrailerUrl(entity.getTrailerUrl());
    movie.setReleaseYear(entity.getReleaseYear());
    movie.setCountry(entity.getCountry());
    movie.setLanguage(entity.getLanguage());
    movie.setAgeRating(entity.getAgeRating());
    movie.setMovieStatus(entity.getMovieStatus());
    movie.setMovieType(entity.getMovieType());
    movie.setIsPremiumOnly(entity.getIsPremiumOnly());
    movie.setViewCount(entity.getViewCount());
    movie.setFavoriteCount(entity.getFavoriteCount());
    movie.setAverageRating(entity.getAverageRating());
    movie.setTotalRatings(entity.getTotalRatings());
    movie.setTotalReviews(entity.getTotalReviews());
    movie.setCreatedAt(entity.getCreatedAt());
    movie.setUpdatedAt(entity.getUpdatedAt());
    movie.setPublishedAt(entity.getPublishedAt());
    return movie;
  }

  private MovieEntity toEntity (Movie movie) {
    MovieEntity entity = new MovieEntity();
    entity.setId(movie.getId());
    entity.setTitle(movie.getTitle());
    entity.setOriginalTitle(movie.getOriginalTitle());
    entity.setSlug(movie.getSlug());
    entity.setDescription(movie.getDescription());
    entity.setPosterUrl(movie.getPosterUrl());
    entity.setBannerUrl(movie.getBannerUrl());
    entity.setTrailerUrl(movie.getTrailerUrl());
    entity.setReleaseYear(movie.getReleaseYear());
    entity.setCountry(movie.getCountry());
    entity.setLanguage(movie.getLanguage());
    entity.setAgeRating(movie.getAgeRating());
    entity.setMovieStatus(movie.getMovieStatus());
    entity.setMovieType(movie.getMovieType());
    entity.setIsPremiumOnly(movie.getIsPremiumOnly());
    entity.setViewCount(movie.getViewCount());
    entity.setFavoriteCount(movie.getFavoriteCount());
    entity.setAverageRating(movie.getAverageRating());
    entity.setTotalRatings(movie.getTotalRatings());
    entity.setTotalReviews(movie.getTotalReviews());
    entity.setCreatedAt(movie.getCreatedAt());
    entity.setUpdatedAt(movie.getUpdatedAt());
    entity.setPublishedAt(movie.getPublishedAt());
    return entity;
  }

  @Override
  public void deleteById (Long id) {
    jpaMovieRepository.deleteById(id);
  }

  @Override
  public List<Movie> findTopTrending (int limit) {
    return jpaMovieRepository.findTopTrending(org.springframework.data.domain.PageRequest.of(0, limit))
        .stream().map(this::toDomain).toList();
  }

  @Override
  public List<Movie> findWeeklyNew (int limit) {
    java.time.LocalDateTime since = java.time.LocalDateTime.now().minusDays(7);
    return jpaMovieRepository
        .findWeeklyNew(since, org.springframework.data.domain.PageRequest.of(0, limit)).stream()
        .map(this::toDomain).toList();
  }

  @Override
  public List<Movie> findTopRated (int limit) {
    return jpaMovieRepository.findTopRated(org.springframework.data.domain.PageRequest.of(0, limit))
        .stream().map(this::toDomain).toList();
  }

  @Override
  public List<Movie> findTopSeries (int limit) {
    return jpaMovieRepository.findTopSeries(org.springframework.data.domain.PageRequest.of(0, limit))
        .stream().map(this::toDomain).toList();
  }

  @Override
  public List<Movie> findUpcoming (int limit) {
    return jpaMovieRepository.findUpcoming(org.springframework.data.domain.PageRequest.of(0, limit))
        .stream().map(this::toDomain).toList();
  }

  @Override
  public List<Movie> findByCountry (String country, int page, int size) {
    return jpaMovieRepository
        .findByCountry(country, org.springframework.data.domain.PageRequest.of(page, size)).stream()
        .map(this::toDomain).toList();
  }

  @Override
  public List<Movie> findTopSeriesByCountry (String country, int limit) {
    return jpaMovieRepository
        .findTopSeriesByCountry(country, org.springframework.data.domain.PageRequest.of(0, limit))
        .stream().map(this::toDomain).toList();
  }

  @Override
  public List<Movie> findTopSeriesDrama (int limit) {
    return jpaMovieRepository.findTopSeriesDrama(org.springframework.data.domain.PageRequest.of(0, limit))
        .stream().map(this::toDomain).toList();
  }

  @Override
  public List<Movie> findActionMovies (int limit) {
    return jpaMovieRepository.findActionMovies(org.springframework.data.domain.PageRequest.of(0, limit))
        .stream().map(this::toDomain).toList();
  }

  @Override
  public List<Movie> findThrillerMovies (int limit) {
    return jpaMovieRepository.findThrillerMovies(org.springframework.data.domain.PageRequest.of(0, limit))
        .stream().map(this::toDomain).toList();
  }

  @Override
  public List<Movie> findAnimeSeries (int limit) {
    return jpaMovieRepository.findAnimeSeries(org.springframework.data.domain.PageRequest.of(0, limit))
        .stream().map(this::toDomain).toList();
  }

  @Override
  public List<Movie> findAnimeMovies (int limit) {
    return jpaMovieRepository.findAnimeMovies(org.springframework.data.domain.PageRequest.of(0, limit))
        .stream().map(this::toDomain).toList();
  }

  @Override
  public List<Movie> findMostCommented (int limit) {
    return jpaMovieRepository.findMostCommented(org.springframework.data.domain.PageRequest.of(0, limit))
        .stream().map(this::toDomain).toList();
  }
}