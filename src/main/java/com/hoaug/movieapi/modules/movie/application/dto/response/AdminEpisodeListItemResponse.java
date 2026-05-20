package com.hoaug.movieapi.modules.movie.application.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.hoaug.movieapi.modules.movie.domain.model.EpisodeStatus;
import com.hoaug.movieapi.modules.movie.domain.model.MovieType;

/**
 * Item dùng cho trang quản trị tập phim. Embed thông tin tóm tắt phim cha để
 * giảm round-trip và đủ dữ liệu cho cột bảng.
 */
public class AdminEpisodeListItemResponse {
  private Long id;
  private String title;
  private Integer episodeNumber;
  private String videoUrl;
  private String thumbnailUrl;
  private Integer durationSeconds;
  private Boolean isFreePreview;
  private EpisodeStatus status;
  private List<String> availableQualities;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  private Long movieId;
  private String movieTitle;
  private String movieSlug;
  private MovieType movieType;
  private Integer movieReleaseYear;
  private String movieCountry;
  private String movieStatus;
  private String moviePosterUrl;

  public Long getId () { return id; }
  public void setId (Long id) { this.id = id; }

  public String getTitle () { return title; }
  public void setTitle (String title) { this.title = title; }

  public Integer getEpisodeNumber () { return episodeNumber; }
  public void setEpisodeNumber (Integer episodeNumber) { this.episodeNumber = episodeNumber; }

  public String getVideoUrl () { return videoUrl; }
  public void setVideoUrl (String videoUrl) { this.videoUrl = videoUrl; }

  public String getThumbnailUrl () { return thumbnailUrl; }
  public void setThumbnailUrl (String thumbnailUrl) { this.thumbnailUrl = thumbnailUrl; }

  public Integer getDurationSeconds () { return durationSeconds; }
  public void setDurationSeconds (Integer durationSeconds) {
    this.durationSeconds = durationSeconds;
  }

  public Boolean getIsFreePreview () { return isFreePreview; }
  public void setIsFreePreview (Boolean freePreview) { isFreePreview = freePreview; }

  public EpisodeStatus getStatus () { return status; }
  public void setStatus (EpisodeStatus status) { this.status = status; }

  public List<String> getAvailableQualities () { return availableQualities; }
  public void setAvailableQualities (List<String> availableQualities) {
    this.availableQualities = availableQualities;
  }

  public LocalDateTime getCreatedAt () { return createdAt; }
  public void setCreatedAt (LocalDateTime createdAt) { this.createdAt = createdAt; }

  public LocalDateTime getUpdatedAt () { return updatedAt; }
  public void setUpdatedAt (LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

  public Long getMovieId () { return movieId; }
  public void setMovieId (Long movieId) { this.movieId = movieId; }

  public String getMovieTitle () { return movieTitle; }
  public void setMovieTitle (String movieTitle) { this.movieTitle = movieTitle; }

  public String getMovieSlug () { return movieSlug; }
  public void setMovieSlug (String movieSlug) { this.movieSlug = movieSlug; }

  public MovieType getMovieType () { return movieType; }
  public void setMovieType (MovieType movieType) { this.movieType = movieType; }

  public Integer getMovieReleaseYear () { return movieReleaseYear; }
  public void setMovieReleaseYear (Integer movieReleaseYear) {
    this.movieReleaseYear = movieReleaseYear;
  }

  public String getMovieCountry () { return movieCountry; }
  public void setMovieCountry (String movieCountry) { this.movieCountry = movieCountry; }

  public String getMovieStatus () { return movieStatus; }
  public void setMovieStatus (String movieStatus) { this.movieStatus = movieStatus; }

  public String getMoviePosterUrl () { return moviePosterUrl; }
  public void setMoviePosterUrl (String moviePosterUrl) { this.moviePosterUrl = moviePosterUrl; }
}
