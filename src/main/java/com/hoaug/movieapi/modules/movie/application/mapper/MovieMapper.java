package com.hoaug.movieapi.modules.movie.application.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.movie.application.dto.response.CategoryResponse;
import com.hoaug.movieapi.modules.movie.application.dto.response.EpisodeResponse;
import com.hoaug.movieapi.modules.movie.application.dto.response.MovieDetailResponse;
import com.hoaug.movieapi.modules.movie.application.dto.response.MoviePersonResponse;
import com.hoaug.movieapi.modules.movie.application.dto.response.MovieStudioResponse;
import com.hoaug.movieapi.modules.movie.application.dto.response.MovieSummaryResponse;
import com.hoaug.movieapi.modules.movie.application.dto.response.MovieBasicResponse;
import com.hoaug.movieapi.modules.movie.application.dto.response.TagResponse;
import com.hoaug.movieapi.modules.movie.domain.model.Episode;
import com.hoaug.movieapi.modules.movie.domain.model.Movie;

@Component
public class MovieMapper {

  public MovieBasicResponse toBasicResponse (Movie movie) {
    MovieBasicResponse res = new MovieBasicResponse();
    res.setId(movie.getId());
    res.setTitle(movie.getTitle());
    res.setSlug(movie.getSlug());
    res.setDescription(movie.getDescription());
    res.setPosterUrl(movie.getPosterUrl());
    res.setBannerUrl(movie.getBannerUrl());
    res.setReleaseYear(movie.getReleaseYear());
    res.setAverageRating(movie.getAverageRating() != null ? movie.getAverageRating().doubleValue() : null);
    res.setViewCount(movie.getViewCount());
    res.setFavoriteCount(movie.getFavoriteCount());
    return res;
  }

  public MovieSummaryResponse toSummaryResponse (Movie movie) {
    return toSummaryResponse(movie, List.of());
  }

  public MovieSummaryResponse toSummaryResponse (Movie movie, List<CategoryResponse> categories) {
    MovieSummaryResponse response = new MovieSummaryResponse();
    response.setId(movie.getId());
    response.setTitle(movie.getTitle());
    response.setOriginalTitle(movie.getOriginalTitle());
    response.setSlug(movie.getSlug());
    response.setDescription(movie.getDescription());
    response.setPosterUrl(movie.getPosterUrl());
    response.setBannerUrl(movie.getBannerUrl());
    response.setTrailerUrl(movie.getTrailerUrl());
    response.setReleaseYear(movie.getReleaseYear());
    response.setCountry(movie.getCountry());
    response.setLanguage(movie.getLanguage());
    response.setAgeRating(movie.getAgeRating());
    response.setMovieType(movie.getMovieType().name());
    response.setMovieStatus(movie.getMovieStatus().name());
    response.setIsPremiumOnly(movie.getIsPremiumOnly());
    response.setViewCount(movie.getViewCount());
    response.setFavoriteCount(movie.getFavoriteCount());
    response.setAverageRating(movie.getAverageRating());
    response.setTotalRatings(movie.getTotalRatings());
    response.setTotalReviews(movie.getTotalReviews());
    response.setCreatedAt(movie.getCreatedAt());
    response.setUpdatedAt(movie.getUpdatedAt());
    response.setPublishedAt(movie.getPublishedAt());
    response.setCategories(categories);
    return response;
  }

  public MovieDetailResponse toDetailResponse (Movie movie, List<Episode> episodes,
      List<CategoryResponse> categories, List<TagResponse> tags, List<MoviePersonResponse> persons,
      List<MovieStudioResponse> studios) {
    MovieDetailResponse response = new MovieDetailResponse();
    response.setId(movie.getId());
    response.setTitle(movie.getTitle());
    response.setOriginalTitle(movie.getOriginalTitle());
    response.setSlug(movie.getSlug());
    response.setDescription(movie.getDescription());
    response.setPosterUrl(movie.getPosterUrl());
    response.setBannerUrl(movie.getBannerUrl());
    response.setTrailerUrl(movie.getTrailerUrl());
    response.setReleaseYear(movie.getReleaseYear());
    response.setCountry(movie.getCountry());
    response.setLanguage(movie.getLanguage());
    response.setAgeRating(movie.getAgeRating());
    response.setMovieStatus(movie.getMovieStatus().name());
    response.setMovieType(movie.getMovieType().name());
    response.setIsPremiumOnly(movie.getIsPremiumOnly());
    response.setViewCount(movie.getViewCount());
    response.setFavoriteCount(movie.getFavoriteCount());
    response.setAverageRating(movie.getAverageRating());
    response.setTotalRatings(movie.getTotalRatings());
    response.setTotalReviews(movie.getTotalReviews());
    response.setPublishedAt(movie.getPublishedAt());
    response.setEpisodes(episodes.stream().map(this::toEpisodeResponse).toList());
    response.setCategories(categories);
    response.setTags(tags);
    response.setPersons(persons);
    response.setStudios(studios);
    return response;
  }

  public EpisodeResponse toEpisodeResponse (Episode episode) {
    EpisodeResponse response = new EpisodeResponse();
    response.setId(episode.getId());
    response.setTitle(episode.getTitle());
    response.setEpisodeNumber(episode.getEpisodeNumber());
    response.setVideoUrl(episode.getVideoUrl());
    response.setThumbnailUrl(episode.getThumbnailUrl());
    response.setDurationSeconds(episode.getDurationSeconds());
    response.setIsFreePreview(episode.getIsFreePreview());
    response.setStatus(episode.getStatus());
    return response;
  }
}