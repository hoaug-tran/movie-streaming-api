package com.hoaug.movieapi.modules.movie.infrastructure.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hoaug.movieapi.modules.movie.domain.model.MovieStatus;
import com.hoaug.movieapi.modules.movie.infrastructure.persistence.entity.MovieEntity;

public interface JpaMovieRepository extends JpaRepository<MovieEntity, Long> {
  List<MovieEntity> findByMovieStatus (MovieStatus movieStatus);

  Optional<MovieEntity> findByIdAndMovieStatus (Long id, MovieStatus movieStatus);

  Optional<MovieEntity> findBySlugAndMovieStatus (String slug, MovieStatus movieStatus);

  boolean existsBySlug (String slug);

  Page<MovieEntity> findByMovieStatus (MovieStatus movieStatus, Pageable pageable);

  Page<MovieEntity> findByMovieStatusAndTitleContaining (MovieStatus movieStatus, String title,
      Pageable pageable);

  // Discovery Queries
  @Query("SELECT m FROM MovieEntity m WHERE m.movieStatus = 'PUBLISHED' ORDER BY m.viewCount DESC")
  List<MovieEntity> findTopTrending (Pageable pageable);

  @Query("SELECT m FROM MovieEntity m WHERE m.movieStatus = 'PUBLISHED' AND m.publishedAt >= :since ORDER BY m.publishedAt DESC")
  List<MovieEntity> findWeeklyNew (@Param("since") java.time.LocalDateTime since, Pageable pageable);

  @Query("SELECT m FROM MovieEntity m WHERE m.movieStatus = 'PUBLISHED' ORDER BY m.averageRating DESC")
  List<MovieEntity> findTopRated (Pageable pageable);

  @Query("SELECT m FROM MovieEntity m WHERE m.movieStatus = 'PUBLISHED' AND m.movieType = 'SERIES' ORDER BY m.viewCount DESC")
  List<MovieEntity> findTopSeries (Pageable pageable);

  @Query("SELECT m FROM MovieEntity m WHERE m.movieStatus = 'UPCOMING' ORDER BY m.createdAt DESC")
  List<MovieEntity> findUpcoming (Pageable pageable);

  @Query("SELECT m FROM MovieEntity m WHERE m.movieStatus = 'PUBLISHED' AND m.country = :country ORDER BY m.publishedAt DESC")
  Page<MovieEntity> findByCountry (@Param("country") String country, Pageable pageable);
}