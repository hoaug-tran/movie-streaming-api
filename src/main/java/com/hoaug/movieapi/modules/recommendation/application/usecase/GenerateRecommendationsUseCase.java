package com.hoaug.movieapi.modules.recommendation.application.usecase;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.favorite.domain.model.Favorite;
import com.hoaug.movieapi.modules.favorite.domain.repository.FavoriteRepository;
import com.hoaug.movieapi.modules.movie.application.mapper.MovieMapper;
import com.hoaug.movieapi.modules.movie.domain.model.MovieStatus;
import com.hoaug.movieapi.modules.movie.infrastructure.persistence.entity.MovieCategoryEntity;
import com.hoaug.movieapi.modules.movie.infrastructure.persistence.entity.MovieEntity;
import com.hoaug.movieapi.modules.movie.infrastructure.persistence.repository.JpaMovieCategoryRepository;
import com.hoaug.movieapi.modules.movie.infrastructure.persistence.repository.JpaMovieRepository;
import com.hoaug.movieapi.modules.recommendation.application.dto.request.GenerateRecommendationsRequest;
import com.hoaug.movieapi.modules.recommendation.application.dto.response.MovieRecommendationResponse;
import com.hoaug.movieapi.modules.recommendation.application.mapper.MovieRecommendationMapper;
import com.hoaug.movieapi.modules.recommendation.domain.model.MovieRecommendation;
import com.hoaug.movieapi.modules.recommendation.domain.repository.MovieRecommendationRepository;
import com.hoaug.movieapi.modules.watchhistory.domain.model.WatchHistory;
import com.hoaug.movieapi.modules.watchhistory.domain.repository.WatchHistoryRepository;

@Component
public class GenerateRecommendationsUseCase {

  private static final int DEFAULT_LIMIT = 12;
  private static final int MAX_LIMIT = 30;
  private static final double FAVORITE_WEIGHT = 3.0;
  private static final double WATCH_COMPLETED_WEIGHT = 2.0;
  private static final double WATCH_PARTIAL_WEIGHT = 1.0;
  private static final double RATING_BOOST = 0.1;
  private static final double FALLBACK_FLOOR = 0.5;

  private final MovieRecommendationRepository movieRecommendationRepository;
  private final MovieRecommendationMapper movieRecommendationMapper;
  private final WatchHistoryRepository watchHistoryRepository;
  private final FavoriteRepository favoriteRepository;
  private final JpaMovieCategoryRepository jpaMovieCategoryRepository;
  private final JpaMovieRepository jpaMovieRepository;
  private final MovieMapper movieMapper;

  public GenerateRecommendationsUseCase(MovieRecommendationRepository movieRecommendationRepository,
      MovieRecommendationMapper movieRecommendationMapper,
      WatchHistoryRepository watchHistoryRepository, FavoriteRepository favoriteRepository,
      JpaMovieCategoryRepository jpaMovieCategoryRepository,
      JpaMovieRepository jpaMovieRepository, MovieMapper movieMapper) {
    this.movieRecommendationRepository = movieRecommendationRepository;
    this.movieRecommendationMapper = movieRecommendationMapper;
    this.watchHistoryRepository = watchHistoryRepository;
    this.favoriteRepository = favoriteRepository;
    this.jpaMovieCategoryRepository = jpaMovieCategoryRepository;
    this.jpaMovieRepository = jpaMovieRepository;
    this.movieMapper = movieMapper;
  }

  @CacheEvict(cacheNames = "recommendations", key = "'user:' + #request.userId + ':recommendations'")
  public List<MovieRecommendationResponse> execute (GenerateRecommendationsRequest request) {
    Long userId = request.getUserId();
    int limit = sanitizeLimit(request.getLimit());

    Map<Long, Double> seedWeights = collectSeedMovieWeights(userId);
    Set<Long> excludeMovieIds = new HashSet<>(seedWeights.keySet());
    Map<Long, Double> categoryWeights = computeCategoryWeights(seedWeights);

    List<ScoredMovie> scored = scoreCandidateMovies(categoryWeights, excludeMovieIds, limit);

    movieRecommendationRepository.deleteAllByUserId(userId);
    LocalDateTime now = LocalDateTime.now();

    for (ScoredMovie item : scored) {
      MovieRecommendation recommendation = new MovieRecommendation();
      recommendation.setUserId(userId);
      recommendation.setMovieId(item.movieId);
      recommendation.setScore(BigDecimal.valueOf(item.score).setScale(2, RoundingMode.HALF_UP));
      recommendation.setReason(item.reason);
      recommendation.setCreatedAt(now);
      movieRecommendationRepository.save(recommendation);
    }

    return movieRecommendationRepository.findByUserIdOrderByScoreDescCreatedAtDesc(userId).stream()
        .map(rec -> {
          MovieRecommendationResponse res = movieRecommendationMapper.toResponse(rec);
          jpaMovieRepository.findById(rec.getMovieId())
              .ifPresent(movie -> res.setMovie(toBasicResponseFromEntity(movie)));
          return res;
        }).toList();
  }

  private int sanitizeLimit (Integer requested) {
    if (requested == null || requested <= 0)
      return DEFAULT_LIMIT;
    return Math.min(requested, MAX_LIMIT);
  }

  private Map<Long, Double> collectSeedMovieWeights (Long userId) {
    Map<Long, Double> seeds = new HashMap<>();

    List<Favorite> favorites = favoriteRepository.findByUserIdOrderByAddedAtDesc(userId);
    for (Favorite favorite : favorites) {
      seeds.merge(favorite.getMovieId(), FAVORITE_WEIGHT, (a, b) -> a + b);
    }

    List<WatchHistory> histories = watchHistoryRepository
        .findByUserIdOrderByLastWatchedAtDesc(userId);
    for (WatchHistory history : histories) {
      double weight = Boolean.TRUE.equals(history.getIsCompleted()) ? WATCH_COMPLETED_WEIGHT
          : WATCH_PARTIAL_WEIGHT;
      seeds.merge(history.getMovieId(), weight, (a, b) -> a + b);
    }
    return seeds;
  }

  private Map<Long, Double> computeCategoryWeights (Map<Long, Double> seedWeights) {
    if (seedWeights.isEmpty())
      return Collections.emptyMap();

    List<MovieCategoryEntity> seedCategories = jpaMovieCategoryRepository
        .findByMovieIdIn(seedWeights.keySet());
    Map<Long, Double> categoryWeights = new HashMap<>();
    for (MovieCategoryEntity mc : seedCategories) {
      Double seedWeight = seedWeights.get(mc.getMovieId());
      if (seedWeight == null)
        continue;
      categoryWeights.merge(mc.getCategoryId(), seedWeight, (a, b) -> a + b);
    }
    return categoryWeights;
  }

  private List<ScoredMovie> scoreCandidateMovies (Map<Long, Double> categoryWeights,
      Set<Long> excludeMovieIds, int limit) {
    List<MovieEntity> publishedMovies = jpaMovieRepository.findByMovieStatus(MovieStatus.PUBLISHED);

    if (categoryWeights.isEmpty()) {
      return publishedMovies.stream()
          .filter(movie -> !excludeMovieIds.contains(movie.getId()))
          .sorted(Comparator
              .comparingDouble(this::popularityScore).reversed())
          .limit(limit)
          .map(movie -> new ScoredMovie(movie.getId(),
              FALLBACK_FLOOR + popularityScore(movie) * RATING_BOOST,
              "Phim đang được yêu thích"))
          .collect(Collectors.toList());
    }

    Set<Long> candidateIds = publishedMovies.stream().map(MovieEntity::getId)
        .filter(id -> !excludeMovieIds.contains(id)).collect(Collectors.toSet());
    if (candidateIds.isEmpty())
      return Collections.emptyList();

    List<MovieCategoryEntity> candidateCategories = jpaMovieCategoryRepository
        .findByMovieIdIn(candidateIds);
    Map<Long, List<Long>> moviesToCategories = new HashMap<>();
    for (MovieCategoryEntity mc : candidateCategories) {
      moviesToCategories.computeIfAbsent(mc.getMovieId(), key -> new ArrayList<>())
          .add(mc.getCategoryId());
    }

    Map<Long, MovieEntity> moviesById = publishedMovies.stream()
        .collect(Collectors.toMap(MovieEntity::getId, movie -> movie));

    List<ScoredMovie> ranked = new ArrayList<>();
    for (Map.Entry<Long, List<Long>> entry : moviesToCategories.entrySet()) {
      Long movieId = entry.getKey();
      List<Long> categoryIds = entry.getValue();
      double overlapScore = 0.0;
      int matchedCategories = 0;
      for (Long categoryId : categoryIds) {
        Double categoryWeight = categoryWeights.get(categoryId);
        if (categoryWeight == null)
          continue;
        overlapScore += categoryWeight;
        matchedCategories++;
      }
      if (matchedCategories == 0)
        continue;

      MovieEntity movie = moviesById.get(movieId);
      if (movie == null)
        continue;

      double finalScore = overlapScore + popularityScore(movie) * RATING_BOOST;
      String reason = matchedCategories == 1 ? "Cùng thể loại bạn yêu thích"
          : "Khớp " + matchedCategories + " thể loại bạn quan tâm";
      ranked.add(new ScoredMovie(movieId, finalScore, reason));
    }

    ranked.sort(Comparator.comparingDouble((ScoredMovie s) -> s.score).reversed());
    if (ranked.size() < limit) {
      Set<Long> already = ranked.stream().map(s -> s.movieId).collect(Collectors.toSet());
      publishedMovies.stream()
          .filter(movie -> !excludeMovieIds.contains(movie.getId()))
          .filter(movie -> !already.contains(movie.getId()))
          .sorted(Comparator.comparingDouble(this::popularityScore).reversed())
          .limit(limit - ranked.size())
          .forEach(movie -> ranked.add(new ScoredMovie(movie.getId(),
              FALLBACK_FLOOR + popularityScore(movie) * RATING_BOOST,
              "Phim hot dành cho bạn")));
    }
    return ranked.stream().limit(limit).collect(Collectors.toList());
  }

  private double popularityScore (MovieEntity movie) {
    double rating = movie.getAverageRating() == null ? 0.0
        : movie.getAverageRating().doubleValue();
    long views = movie.getViewCount() == null ? 0L : movie.getViewCount();
    return rating + Math.log10(Math.max(1.0, views));
  }

  private com.hoaug.movieapi.modules.movie.application.dto.response.MovieBasicResponse toBasicResponseFromEntity (
      MovieEntity entity) {
    com.hoaug.movieapi.modules.movie.domain.model.Movie movie = new com.hoaug.movieapi.modules.movie.domain.model.Movie();
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
    movie.setPublishedAt(entity.getPublishedAt());
    return movieMapper.toBasicResponse(movie);
  }

  private static class ScoredMovie {
    final Long movieId;
    final double score;
    final String reason;

    ScoredMovie(Long movieId, double score, String reason) {
      this.movieId = movieId;
      this.score = score;
      this.reason = reason;
    }
  }
}