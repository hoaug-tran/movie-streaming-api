package com.hoaug.movieapi.modules.streaming.application.dto.response;

import java.time.Instant;
import java.util.List;
import java.util.Set;

public class TranscodeProgressResponse {
  public Long episodeId;
  public String status;
  public List<String> targetQualities;
  public Set<String> completedQualities;
  public Set<String> failedQualities;
  public Set<String> skippedQualities;
  public String currentQuality;
  public String message;
  public Instant startedAt;
  public Instant updatedAt;
  public Instant finishedAt;
  public int percent;

  public TranscodeProgressResponse() {
  }
}
