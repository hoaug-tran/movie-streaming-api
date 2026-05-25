package com.hoaug.movieapi.modules.streaming.application.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.movie.infrastructure.persistence.repository.JpaEpisodeRepository;
import com.hoaug.movieapi.modules.streaming.application.config.MediaStorageProperties;
import com.hoaug.movieapi.modules.streaming.application.dto.request.HlsTranscodeRequest;

@Component
public class AsyncTranscodeService {

  private static final Logger log = LoggerFactory.getLogger(AsyncTranscodeService.class);
  private static final List<String> QUALITIES = List.of("720p", "1080p", "4K");

  private final HlsTranscodeService hlsTranscodeService;
  private final HlsPathService hlsPathService;
  private final StreamUrlService streamUrlService;
  private final JpaEpisodeRepository episodeRepository;
  private final MediaStorageProperties storageProperties;
  private final TranscodeProgressTracker progressTracker;

  public AsyncTranscodeService(HlsTranscodeService hlsTranscodeService,
      HlsPathService hlsPathService, StreamUrlService streamUrlService,
      JpaEpisodeRepository episodeRepository, MediaStorageProperties storageProperties,
      TranscodeProgressTracker progressTracker) {
    this.hlsTranscodeService = hlsTranscodeService;
    this.hlsPathService = hlsPathService;
    this.streamUrlService = streamUrlService;
    this.episodeRepository = episodeRepository;
    this.storageProperties = storageProperties;
    this.progressTracker = progressTracker;
  }

  public void markQueued (Long episodeId) {
    progressTracker.start(episodeId, QUALITIES);
  }

  @Async("transcodeExecutor")
  public void transcodeEpisodeAsync (Long episodeId, Path sourcePath) {
    log.info("[Transcode] Starting HLS transcode for episode {} from {}", episodeId, sourcePath);
    progressTracker.start(episodeId, QUALITIES);
    StringBuilder completedQualities = new StringBuilder();
    boolean videoUrlSet = false;

    String version = "v" + Long.toString(System.currentTimeMillis(), 36)
        + Integer.toString(ThreadLocalRandom.current().nextInt(0x10000), 36);

    for (String quality : QUALITIES) {
      try {
        log.info("[Transcode] Episode {} → quality {} (version={})", episodeId, quality, version);
        progressTracker.quality(episodeId, quality);
        Path outputDir = hlsPathService.episodeOutputDirectory(episodeId, quality);
        Path keyPath = hlsPathService.episodeKeyPath(episodeId, quality);
        String keyUri = streamUrlService.episodeKeyUrl(episodeId, quality);
        String playlistUrl = streamUrlService.episodePlaylistUrl(episodeId, quality);

        HlsTranscodeRequest request = new HlsTranscodeRequest(sourcePath, outputDir, keyPath,
            keyUri, version);
        hlsTranscodeService.transcodeQuality(request, quality, playlistUrl);

        if (completedQualities.length() > 0)
          completedQualities.append(",");
        completedQualities.append(quality);

        final boolean isFirstReady = !videoUrlSet;
        episodeRepository.findById(episodeId).ifPresent(ep -> {
          ep.setAvailableQualities(completedQualities.toString());
          ep.setTranscodeVersion(version);
          if (isFirstReady) {
            ep.setVideoUrl(playlistUrl);
            log.info("[Transcode] Episode {} videoUrl set early to {} (first ready quality)",
                episodeId, playlistUrl);
          }
          episodeRepository.save(ep);
        });
        if (isFirstReady)
          videoUrlSet = true;

        progressTracker.completed(episodeId, quality);
        log.info("[Transcode] Episode {} → {} DONE", episodeId, quality);
      } catch (AppException e) {
        if (ErrorCode.BAD_REQUEST.equals(e.getErrorCode())) {
          progressTracker.skipped(episodeId, quality, "source resolution too low");
          log.info("[Transcode] Episode {} → {} SKIPPED (source resolution too low)", episodeId,
              quality);
        } else {
          progressTracker.failed(episodeId, quality, e.getMessage());
          log.error("[Transcode] Episode {} → {} FAILED: {}", episodeId, quality, e.getMessage(),
              e);
        }
      } catch (Exception e) {
        progressTracker.failed(episodeId, quality, e.getMessage());
        log.error("[Transcode] Episode {} → {} FAILED: {}", episodeId, quality, e.getMessage(), e);
      }
    }

    final boolean videoUrlAlreadySet = videoUrlSet;
    episodeRepository.findById(episodeId).ifPresent(ep -> {
      if (!videoUrlAlreadySet) {
        String hlsUrl = streamUrlService.episodePlaylistUrl(episodeId, "720p");
        ep.setVideoUrl(hlsUrl);
        log.warn("[Transcode] Episode {} no quality succeeded, fallback videoUrl={}", episodeId,
            hlsUrl);
      }
      if (ep.getThumbnailUrl() == null || ep.getThumbnailUrl().isBlank()) {
        try {
          String thumbUrl = extractThumbnail(sourcePath, episodeId);
          if (thumbUrl != null)
            ep.setThumbnailUrl(thumbUrl);
        } catch (Exception ex) {
          log.warn("[Transcode] Episode {} thumbnail extraction failed: {}", episodeId,
              ex.getMessage());
        }
      }
      episodeRepository.save(ep);
      log.info("[Transcode] Episode {} transcode complete. qualities={}", episodeId,
          completedQualities);
    });
    progressTracker.finish(episodeId);
  }

  private String extractThumbnail (Path sourcePath, Long episodeId)
      throws IOException, InterruptedException {
    Path ffmpegBin = Path.of(storageProperties.getFfmpegPath()).toAbsolutePath().normalize()
        .getParent();
    Path ffmpegPath = ffmpegBin != null ? ffmpegBin.resolve("ffmpeg.exe") : Path.of("ffmpeg");
    if (!Files.isRegularFile(ffmpegPath))
      ffmpegPath = Path.of("ffmpeg");

    String filename = "ep-" + episodeId + "-" + UUID.randomUUID().toString().substring(0, 8)
        + ".jpg";
    Path imagesDir = Path.of(storageProperties.getImagesDirectory()).toAbsolutePath().normalize();
    Files.createDirectories(imagesDir);
    Path outputPath = imagesDir.resolve(filename);

    List<String> cmd = List.of(ffmpegPath.toString(), "-y", "-ss", "5", "-i",
        sourcePath.toAbsolutePath().normalize().toString(), "-frames:v", "1", "-q:v", "3", "-vf",
        "scale=1280:-2", outputPath.toString());

    log.info("[Thumbnail] Extracting thumbnail for episode {} → {}", episodeId, filename);
    Process process = new ProcessBuilder(cmd).redirectErrorStream(true).start();
    byte[] out = process.getInputStream().readAllBytes();
    int exit = process.waitFor();
    if (exit != 0) {
      log.warn("[Thumbnail] ffmpeg exit={} output={}", exit,
          new String(out, StandardCharsets.UTF_8));
      return null;
    }
    String url = streamUrlService.imageUrl(filename);
    log.info("[Thumbnail] Episode {} thumbnail saved: {}", episodeId, url);
    return url;
  }
}
