package com.hoaug.movieapi.modules.streaming.application.dto.response;

public class MediaUploadResponse {
  private Long id;
  private String videoUrl;
  private String status;

  public MediaUploadResponse(Long id, String videoUrl, String status) {
    this.id = id;
    this.videoUrl = videoUrl;
    this.status = status;
  }

  public Long getId () { return id; }
  public String getVideoUrl () { return videoUrl; }
  public String getStatus () { return status; }
}
