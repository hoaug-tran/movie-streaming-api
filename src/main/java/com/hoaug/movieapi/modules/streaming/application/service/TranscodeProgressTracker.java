package com.hoaug.movieapi.modules.streaming.application.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import org.springframework.stereotype.Component;

@Component
public class TranscodeProgressTracker {

  public enum Status { PENDING, RUNNING, DONE, FAILED }

  public static class EpisodeProgress {
    public Long episodeId;
    public Status status;
    public List<String> targetQualities = new ArrayList<>();
    public Set<String> completedQualities = new CopyOnWriteArraySet<>();
    public Set<String> failedQualities = new CopyOnWriteArraySet<>();
    public Set<String> skippedQualities = new CopyOnWriteArraySet<>();
    public String currentQuality;
    public String message;
    public Instant startedAt;
    public Instant updatedAt;
    public Instant finishedAt;
  }

  private final Map<Long, EpisodeProgress> jobs = new ConcurrentHashMap<>();

  public void start(Long episodeId, List<String> qualities) {
    EpisodeProgress p = new EpisodeProgress();
    p.episodeId = episodeId;
    p.status = Status.PENDING;
    p.targetQualities = new ArrayList<>(qualities);
    p.startedAt = Instant.now();
    p.updatedAt = p.startedAt;
    jobs.put(episodeId, p);
  }

  public void quality(Long episodeId, String quality) {
    EpisodeProgress p = jobs.get(episodeId);
    if (p == null) return;
    p.status = Status.RUNNING;
    p.currentQuality = quality;
    p.updatedAt = Instant.now();
  }

  public void completed(Long episodeId, String quality) {
    EpisodeProgress p = jobs.get(episodeId);
    if (p == null) return;
    p.completedQualities.add(quality);
    p.updatedAt = Instant.now();
  }

  public void skipped(Long episodeId, String quality, String reason) {
    EpisodeProgress p = jobs.get(episodeId);
    if (p == null) return;
    p.skippedQualities.add(quality);
    p.message = reason;
    p.updatedAt = Instant.now();
  }

  public void failed(Long episodeId, String quality, String reason) {
    EpisodeProgress p = jobs.get(episodeId);
    if (p == null) return;
    p.failedQualities.add(quality);
    p.message = reason;
    p.updatedAt = Instant.now();
  }

  public void finish(Long episodeId) {
    EpisodeProgress p = jobs.get(episodeId);
    if (p == null) return;
    p.currentQuality = null;
    p.finishedAt = Instant.now();
    p.updatedAt = p.finishedAt;
    if (!p.completedQualities.isEmpty()) {
      p.status = Status.DONE;
    } else if (!p.failedQualities.isEmpty()) {
      p.status = Status.FAILED;
    } else {
      p.status = Status.DONE;
    }
  }

  public EpisodeProgress get(Long episodeId) {
    return jobs.get(episodeId);
  }

  public Map<Long, EpisodeProgress> snapshot() {
    return Map.copyOf(jobs);
  }

  public void clear(Long episodeId) {
    jobs.remove(episodeId);
  }
}
