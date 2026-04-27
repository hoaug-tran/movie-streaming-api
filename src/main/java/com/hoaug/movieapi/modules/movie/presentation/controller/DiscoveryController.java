package com.hoaug.movieapi.modules.movie.presentation.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hoaug.movieapi.common.response.ResponseUtil;
import com.hoaug.movieapi.modules.comment.application.dto.response.CommentResponse;
import com.hoaug.movieapi.modules.comment.application.usecase.GetMostActiveMoviesUseCase;
import com.hoaug.movieapi.modules.comment.application.usecase.GetNewCommentsUseCase;
import com.hoaug.movieapi.modules.comment.application.usecase.GetTopCommentsUseCase;
import com.hoaug.movieapi.modules.movie.application.dto.response.MovieListResponse;
import com.hoaug.movieapi.modules.movie.application.usecase.GetActionMoviesUseCase;
import com.hoaug.movieapi.modules.movie.application.usecase.GetAnimeMoviesUseCase;
import com.hoaug.movieapi.modules.movie.application.usecase.GetAnimeSeriesUseCase;
import com.hoaug.movieapi.modules.movie.application.usecase.GetMoviesByRegionUseCase;
import com.hoaug.movieapi.modules.movie.application.usecase.GetMostCommentedMoviesUseCase;
import com.hoaug.movieapi.modules.movie.application.usecase.GetThrillerMoviesUseCase;
import com.hoaug.movieapi.modules.movie.application.usecase.GetTopRatedMoviesUseCase;
import com.hoaug.movieapi.modules.movie.application.usecase.GetTopSeriesByRegionUseCase;
import com.hoaug.movieapi.modules.movie.application.usecase.GetTopSeriesDramaUseCase;
import com.hoaug.movieapi.modules.movie.application.usecase.GetTopSeriesUseCase;
import com.hoaug.movieapi.modules.movie.application.usecase.GetTrendingMoviesUseCase;
import com.hoaug.movieapi.modules.movie.application.usecase.GetUpcomingMoviesUseCase;
import com.hoaug.movieapi.modules.movie.application.usecase.GetWeeklyNewMoviesUseCase;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/discovery")
@Tag(name = "Discovery", description = "Discovery APIs for homepage sections")
public class DiscoveryController {

  private final GetTrendingMoviesUseCase getTrendingMoviesUseCase;
  private final GetWeeklyNewMoviesUseCase getWeeklyNewMoviesUseCase;
  private final GetUpcomingMoviesUseCase getUpcomingMoviesUseCase;
  private final GetTopRatedMoviesUseCase getTopRatedMoviesUseCase;
  private final GetTopSeriesUseCase getTopSeriesUseCase;
  private final GetMoviesByRegionUseCase getMoviesByRegionUseCase;
  private final GetTopCommentsUseCase getTopCommentsUseCase;
  private final GetNewCommentsUseCase getNewCommentsUseCase;
  private final GetMostActiveMoviesUseCase getMostActiveMoviesUseCase;
  private final GetTopSeriesDramaUseCase getTopSeriesDramaUseCase;
  private final GetActionMoviesUseCase getActionMoviesUseCase;
  private final GetThrillerMoviesUseCase getThrillerMoviesUseCase;
  private final GetAnimeSeriesUseCase getAnimeSeriesUseCase;
  private final GetAnimeMoviesUseCase getAnimeMoviesUseCase;
  private final GetMostCommentedMoviesUseCase getMostCommentedMoviesUseCase;
  private final GetTopSeriesByRegionUseCase getTopSeriesByRegionUseCase;

  public DiscoveryController(GetTrendingMoviesUseCase getTrendingMoviesUseCase,
      GetWeeklyNewMoviesUseCase getWeeklyNewMoviesUseCase,
      GetUpcomingMoviesUseCase getUpcomingMoviesUseCase,
      GetTopRatedMoviesUseCase getTopRatedMoviesUseCase, GetTopSeriesUseCase getTopSeriesUseCase,
      GetMoviesByRegionUseCase getMoviesByRegionUseCase,
      GetTopCommentsUseCase getTopCommentsUseCase, GetNewCommentsUseCase getNewCommentsUseCase,
      GetMostActiveMoviesUseCase getMostActiveMoviesUseCase,
      GetTopSeriesDramaUseCase getTopSeriesDramaUseCase,
      GetActionMoviesUseCase getActionMoviesUseCase,
      GetThrillerMoviesUseCase getThrillerMoviesUseCase,
      GetAnimeSeriesUseCase getAnimeSeriesUseCase,
      GetAnimeMoviesUseCase getAnimeMoviesUseCase,
      GetMostCommentedMoviesUseCase getMostCommentedMoviesUseCase,
      GetTopSeriesByRegionUseCase getTopSeriesByRegionUseCase) {
    this.getTrendingMoviesUseCase = getTrendingMoviesUseCase;
    this.getWeeklyNewMoviesUseCase = getWeeklyNewMoviesUseCase;
    this.getUpcomingMoviesUseCase = getUpcomingMoviesUseCase;
    this.getTopRatedMoviesUseCase = getTopRatedMoviesUseCase;
    this.getTopSeriesUseCase = getTopSeriesUseCase;
    this.getMoviesByRegionUseCase = getMoviesByRegionUseCase;
    this.getTopCommentsUseCase = getTopCommentsUseCase;
    this.getNewCommentsUseCase = getNewCommentsUseCase;
    this.getMostActiveMoviesUseCase = getMostActiveMoviesUseCase;
    this.getTopSeriesDramaUseCase = getTopSeriesDramaUseCase;
    this.getActionMoviesUseCase = getActionMoviesUseCase;
    this.getThrillerMoviesUseCase = getThrillerMoviesUseCase;
    this.getAnimeSeriesUseCase = getAnimeSeriesUseCase;
    this.getAnimeMoviesUseCase = getAnimeMoviesUseCase;
    this.getMostCommentedMoviesUseCase = getMostCommentedMoviesUseCase;
    this.getTopSeriesByRegionUseCase = getTopSeriesByRegionUseCase;
  }

  @GetMapping("/trending")
  @Operation(summary = "Get trending movies")
  public ResponseEntity<MovieListResponse> getTrending (@RequestParam(defaultValue = "10") int limit) {
    return ResponseUtil.ok(getTrendingMoviesUseCase.execute(limit));
  }

  @GetMapping("/weekly-new")
  @Operation(summary = "Get new movies this week")
  public ResponseEntity<MovieListResponse> getWeeklyNew (@RequestParam(defaultValue = "10") int limit) {
    return ResponseUtil.ok(getWeeklyNewMoviesUseCase.execute(limit));
  }

  @GetMapping("/upcoming")
  @Operation(summary = "Get upcoming movies")
  public ResponseEntity<MovieListResponse> getUpcoming (@RequestParam(defaultValue = "10") int limit) {
    return ResponseUtil.ok(getUpcomingMoviesUseCase.execute(limit));
  }

  @GetMapping("/top-rated")
  @Operation(summary = "Get top rated movies")
  public ResponseEntity<MovieListResponse> getTopRated (@RequestParam(defaultValue = "10") int limit) {
    return ResponseUtil.ok(getTopRatedMoviesUseCase.execute(limit));
  }

  @GetMapping("/top-series")
  @Operation(summary = "Get top 10 series today")
  public ResponseEntity<MovieListResponse> getTopSeries (@RequestParam(defaultValue = "10") int limit) {
    return ResponseUtil.ok(getTopSeriesUseCase.execute(limit));
  }

  @GetMapping("/region")
  @Operation(summary = "Get movies by region (Korea, China, etc.)")
  public ResponseEntity<MovieListResponse> getByRegion (@RequestParam String country,
      @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
    return ResponseUtil.ok(getMoviesByRegionUseCase.execute(country, page, size));
  }

  @GetMapping("/top-comments")
  @Operation(summary = "Get top comments")
  public ResponseEntity<List<CommentResponse>> getTopComments (
      @RequestParam(defaultValue = "5") int limit) {
    return ResponseUtil.ok(getTopCommentsUseCase.execute(limit));
  }

  @GetMapping("/new-comments")
  @Operation(summary = "Get new comments")
  public ResponseEntity<List<CommentResponse>> getNewComments (
      @RequestParam(defaultValue = "5") int limit) {
    return ResponseUtil.ok(getNewCommentsUseCase.execute(limit));
  }

  @GetMapping("/most-active")
  @Operation(summary = "Get movies with most comments")
  public ResponseEntity<MovieListResponse> getMostActive (
      @RequestParam(defaultValue = "10") int limit) {
    return ResponseUtil.ok(getMostActiveMoviesUseCase.execute(limit));
  }

  @GetMapping("/series-drama")
  @Operation(summary = "Get top drama/romance series")
  public ResponseEntity<MovieListResponse> getSeriesDrama (@RequestParam(defaultValue = "10") int limit) {
    return ResponseUtil.ok(getTopSeriesDramaUseCase.execute(limit));
  }

  @GetMapping("/action-movies")
  @Operation(summary = "Get top action movies")
  public ResponseEntity<MovieListResponse> getActionMovies (@RequestParam(defaultValue = "10") int limit) {
    return ResponseUtil.ok(getActionMoviesUseCase.execute(limit));
  }

  @GetMapping("/thriller-movies")
  @Operation(summary = "Get top thriller movies")
  public ResponseEntity<MovieListResponse> getThrillerMovies (@RequestParam(defaultValue = "10") int limit) {
    return ResponseUtil.ok(getThrillerMoviesUseCase.execute(limit));
  }

  @GetMapping("/anime-series")
  @Operation(summary = "Get top anime series")
  public ResponseEntity<MovieListResponse> getAnimeSeries (@RequestParam(defaultValue = "10") int limit) {
    return ResponseUtil.ok(getAnimeSeriesUseCase.execute(limit));
  }

  @GetMapping("/anime-movies")
  @Operation(summary = "Get top anime movies")
  public ResponseEntity<MovieListResponse> getAnimeMovies (@RequestParam(defaultValue = "10") int limit) {
    return ResponseUtil.ok(getAnimeMoviesUseCase.execute(limit));
  }

  @GetMapping("/most-commented")
  @Operation(summary = "Get most commented content")
  public ResponseEntity<MovieListResponse> getMostCommented (@RequestParam(defaultValue = "10") int limit) {
    return ResponseUtil.ok(getMostCommentedMoviesUseCase.execute(limit));
  }

  @GetMapping("/top-series-region")
  @Operation(summary = "Get top series by region/country")
  public ResponseEntity<MovieListResponse> getTopSeriesByRegion (@RequestParam String country,
      @RequestParam(defaultValue = "10") int limit) {
    return ResponseUtil.ok(getTopSeriesByRegionUseCase.execute(country, limit));
  }
}