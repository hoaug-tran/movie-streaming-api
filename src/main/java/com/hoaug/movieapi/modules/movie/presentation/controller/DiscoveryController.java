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
import com.hoaug.movieapi.modules.movie.application.usecase.GetMoviesByRegionUseCase;
import com.hoaug.movieapi.modules.movie.application.usecase.GetTopRatedMoviesUseCase;
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

  public DiscoveryController(GetTrendingMoviesUseCase getTrendingMoviesUseCase,
      GetWeeklyNewMoviesUseCase getWeeklyNewMoviesUseCase,
      GetUpcomingMoviesUseCase getUpcomingMoviesUseCase,
      GetTopRatedMoviesUseCase getTopRatedMoviesUseCase, GetTopSeriesUseCase getTopSeriesUseCase,
      GetMoviesByRegionUseCase getMoviesByRegionUseCase,
      GetTopCommentsUseCase getTopCommentsUseCase, GetNewCommentsUseCase getNewCommentsUseCase,
      GetMostActiveMoviesUseCase getMostActiveMoviesUseCase) {
    this.getTrendingMoviesUseCase = getTrendingMoviesUseCase;
    this.getWeeklyNewMoviesUseCase = getWeeklyNewMoviesUseCase;
    this.getUpcomingMoviesUseCase = getUpcomingMoviesUseCase;
    this.getTopRatedMoviesUseCase = getTopRatedMoviesUseCase;
    this.getTopSeriesUseCase = getTopSeriesUseCase;
    this.getMoviesByRegionUseCase = getMoviesByRegionUseCase;
    this.getTopCommentsUseCase = getTopCommentsUseCase;
    this.getNewCommentsUseCase = getNewCommentsUseCase;
    this.getMostActiveMoviesUseCase = getMostActiveMoviesUseCase;
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
}