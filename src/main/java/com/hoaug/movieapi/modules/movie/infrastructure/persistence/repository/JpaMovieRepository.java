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
  @Query(value = "SELECT m.* FROM movies m ORDER BY m.view_count DESC LIMIT 10", nativeQuery = true)
  List<MovieEntity> findTop10Viewed ();

  @Query(value = "SELECT m.* FROM movies m ORDER BY m.average_rating DESC LIMIT 10", nativeQuery = true)
  List<MovieEntity> findTop10Rated ();

  @Query(value = "SELECT m.* FROM movies m ORDER BY m.created_at DESC LIMIT 10", nativeQuery = true)
  List<MovieEntity> findTop10Latest ();

  List<MovieEntity> findByMovieStatus (MovieStatus movieStatus);

  Optional<MovieEntity> findByIdAndMovieStatus (Long id, MovieStatus movieStatus);

  Optional<MovieEntity> findBySlugAndMovieStatus (String slug, MovieStatus movieStatus);

  boolean existsBySlug (String slug);

  Page<MovieEntity> findByMovieStatus (MovieStatus movieStatus, Pageable pageable);

  Page<MovieEntity> findByMovieStatusAndTitleContaining (MovieStatus movieStatus, String title,
      Pageable pageable);

  @Query("SELECT m FROM MovieEntity m WHERE m.movieStatus = :movieStatus AND EXISTS (SELECT 1 FROM MovieCategoryEntity mc WHERE mc.movieId = m.id AND mc.categoryId = :categoryId)")
  Page<MovieEntity> findByMovieStatusAndCategoryId (@Param("movieStatus") MovieStatus movieStatus,
      @Param("categoryId") Long categoryId, Pageable pageable);

  @Query("SELECT m FROM MovieEntity m WHERE m.movieStatus = :movieStatus AND LOWER(m.title) LIKE LOWER(CONCAT('%', :title, '%')) AND EXISTS (SELECT 1 FROM MovieCategoryEntity mc WHERE mc.movieId = m.id AND mc.categoryId = :categoryId)")
  Page<MovieEntity> findByMovieStatusAndCategoryIdAndTitleContaining (
      @Param("movieStatus") MovieStatus movieStatus, @Param("categoryId") Long categoryId,
      @Param("title") String title, Pageable pageable);

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

  @Query("SELECT m FROM MovieEntity m WHERE m.movieStatus = 'PUBLISHED' AND m.movieType = 'SERIES' AND m.country = :country ORDER BY m.viewCount DESC")
  List<MovieEntity> findTopSeriesByCountry (@Param("country") String country, Pageable pageable);

  @Query(value = "SELECT m.* FROM movies m LEFT JOIN movie_categories mc ON m.id = mc.movie_id LEFT JOIN categories c ON mc.category_id = c.id WHERE m.movie_status = 'PUBLISHED' AND m.movie_type = 'SERIES' AND c.slug IN ('lang-man', 'ky-ao', 'tam-ly') ORDER BY m.average_rating DESC", nativeQuery = true)
  List<MovieEntity> findTopSeriesDrama (Pageable pageable);

  @Query(value = "SELECT m.* FROM movies m LEFT JOIN movie_categories mc ON m.id = mc.movie_id LEFT JOIN categories c ON mc.category_id = c.id WHERE m.movie_status = 'PUBLISHED' AND m.movie_type = 'SINGLE' AND c.slug IN ('hanh-dong', 'phieu-luu') ORDER BY m.view_count DESC", nativeQuery = true)
  List<MovieEntity> findActionMovies (Pageable pageable);

  @Query(value = "SELECT m.* FROM movies m LEFT JOIN movie_categories mc ON m.id = mc.movie_id LEFT JOIN categories c ON mc.category_id = c.id WHERE m.movie_status = 'PUBLISHED' AND m.movie_type = 'SINGLE' AND c.slug IN ('kinh-di', 'toi-pham') ORDER BY m.average_rating DESC", nativeQuery = true)
  List<MovieEntity> findThrillerMovies (Pageable pageable);

  @Query(value = "SELECT m.* FROM movies m LEFT JOIN movie_categories mc ON m.id = mc.movie_id LEFT JOIN categories c ON mc.category_id = c.id WHERE m.movie_status = 'PUBLISHED' AND m.movie_type = 'SERIES' AND c.slug = 'hoat-hinh' ORDER BY m.view_count DESC", nativeQuery = true)
  List<MovieEntity> findAnimeSeries (Pageable pageable);

  @Query(value = "SELECT m.* FROM movies m LEFT JOIN movie_categories mc ON m.id = mc.movie_id LEFT JOIN categories c ON mc.category_id = c.id WHERE m.movie_status = 'PUBLISHED' AND m.movie_type = 'SINGLE' AND c.slug = 'hoat-hinh' ORDER BY m.view_count DESC", nativeQuery = true)
  List<MovieEntity> findAnimeMovies (Pageable pageable);

  @Query(value = "SELECT m.* FROM movies m WHERE m.movie_status = 'PUBLISHED' ORDER BY (SELECT COUNT(*) FROM comments c WHERE c.movie_id = m.id) DESC, m.view_count DESC", nativeQuery = true)
  List<MovieEntity> findMostCommented (Pageable pageable);

  @Query("SELECT m FROM MovieEntity m WHERE m.movieStatus = 'PUBLISHED' AND m.movieType = 'SINGLE' ORDER BY m.viewCount DESC")
  List<MovieEntity> findTopSingleMovies (Pageable pageable);

  @Query("SELECT m FROM MovieEntity m WHERE m.movieStatus = 'PUBLISHED' AND m.movieType = 'SERIES' ORDER BY m.viewCount DESC")
  List<MovieEntity> findTopSeriesMovies (Pageable pageable);

  @Query(value = "SELECT m.* FROM movies m WHERE m.movie_status = 'PUBLISHED' ORDER BY (m.view_count + m.favorite_count + (m.total_reviews * 8) + (SELECT COUNT(*) * 4 FROM comments c WHERE c.movie_id = m.id)) DESC", nativeQuery = true)
  List<MovieEntity> findMostInteractedMovies (Pageable pageable);

  long countByMovieStatus (MovieStatus movieStatus);

  long countByMovieType (com.hoaug.movieapi.modules.movie.domain.model.MovieType movieType);

  long countByIsPremiumOnlyTrue ();

  long countByPublishedAtAfter (java.time.LocalDateTime publishedAt);

  @Query("SELECT COALESCE(SUM(m.viewCount), 0) FROM MovieEntity m")
  long sumViewCount ();

  @Query("SELECT COALESCE(SUM(m.favoriteCount), 0) FROM MovieEntity m")
  long sumFavoriteCount ();

  @Query("SELECT COALESCE(AVG(m.averageRating), 0) FROM MovieEntity m")
  java.math.BigDecimal averageRatingAcrossCatalog ();
}
