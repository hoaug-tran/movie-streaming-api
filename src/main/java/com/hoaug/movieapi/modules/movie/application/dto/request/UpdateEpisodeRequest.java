package com.hoaug.movieapi.modules.movie.application.dto.request;

import com.hoaug.movieapi.modules.movie.domain.model.EpisodeStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UpdateEpisodeRequest {

  @NotBlank
  private String title;

  @NotNull
  private Integer episodeNumber;

  private String thumbnailUrl;

  @NotNull
  private Integer durationSeconds;

  @NotNull
  private Boolean isFreePreview;

  @NotNull
  private EpisodeStatus status;

  public String getTitle () { return title; }
  public void setTitle (String title) { this.title = title; }

  public Integer getEpisodeNumber () { return episodeNumber; }
  public void setEpisodeNumber (Integer episodeNumber) { this.episodeNumber = episodeNumber; }

  public String getThumbnailUrl () { return thumbnailUrl; }
  public void setThumbnailUrl (String thumbnailUrl) { this.thumbnailUrl = thumbnailUrl; }

  public Integer getDurationSeconds () { return durationSeconds; }
  public void setDurationSeconds (Integer durationSeconds) { this.durationSeconds = durationSeconds; }

  public Boolean getIsFreePreview () { return isFreePreview; }
  public void setIsFreePreview (Boolean isFreePreview) { this.isFreePreview = isFreePreview; }

  public EpisodeStatus getStatus () { return status; }
  public void setStatus (EpisodeStatus status) { this.status = status; }
}
