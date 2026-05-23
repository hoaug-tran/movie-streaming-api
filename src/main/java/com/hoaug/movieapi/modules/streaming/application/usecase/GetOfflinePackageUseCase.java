package com.hoaug.movieapi.modules.streaming.application.usecase;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.movie.infrastructure.persistence.entity.EpisodeEntity;
import com.hoaug.movieapi.modules.movie.infrastructure.persistence.entity.MovieEntity;
import com.hoaug.movieapi.modules.movie.infrastructure.persistence.repository.JpaEpisodeRepository;
import com.hoaug.movieapi.modules.movie.infrastructure.persistence.repository.JpaMovieRepository;
import com.hoaug.movieapi.modules.streaming.application.service.HlsPathService;
import com.hoaug.movieapi.modules.streaming.application.service.OfflineTokenService;
import com.hoaug.movieapi.modules.streaming.application.service.StreamUrlService;
import com.hoaug.movieapi.modules.streaming.application.service.SubscriptionAccessService;

@Component
public class GetOfflinePackageUseCase {

  private final JpaEpisodeRepository episodeRepository;
  private final JpaMovieRepository movieRepository;
  private final HlsPathService hlsPathService;
  private final StreamUrlService streamUrlService;
  private final OfflineTokenService offlineTokenService;
  private final SubscriptionAccessService subscriptionAccessService;

  public GetOfflinePackageUseCase(JpaEpisodeRepository episodeRepository,
      JpaMovieRepository movieRepository, HlsPathService hlsPathService,
      StreamUrlService streamUrlService, OfflineTokenService offlineTokenService,
      SubscriptionAccessService subscriptionAccessService) {
    this.episodeRepository = episodeRepository;
    this.movieRepository = movieRepository;
    this.hlsPathService = hlsPathService;
    this.streamUrlService = streamUrlService;
    this.offlineTokenService = offlineTokenService;
    this.subscriptionAccessService = subscriptionAccessService;
  }

  private String normalizeQuality (String quality) {
    if (quality == null || quality.isBlank())
      return "720p";

    String q = quality.trim().toUpperCase();

    if ("4K".equals(q) || "2160P".equals(q) || "UHD".equals(q)) {
      return "4K";
    }

    if ("1080P".equals(q) || "FHD".equals(q) || "FULL_HD".equals(q)) {
      return "1080p";
    }

    return "720p";
  }

  public OfflinePackageResponse execute (Long userId, Long episodeId, String quality) {
    String normalizedQuality = normalizeQuality(quality);

    EpisodeEntity episode = episodeRepository.findById(episodeId)
        .orElseThrow( () -> new AppException(ErrorCode.EPISODE_NOT_FOUND));

    MovieEntity movie = movieRepository.findById(episode.getMovieId())
        .orElseThrow( () -> new AppException(ErrorCode.MOVIE_NOT_FOUND));

    if (!subscriptionAccessService.canDownloadOffline(userId)) {
      throw new AppException(ErrorCode.FORBIDDEN);
    }

    if (!subscriptionAccessService.canAccessQuality(userId, normalizedQuality)) {
      throw new AppException(ErrorCode.FORBIDDEN);
    }

    Path hlsDir = hlsPathService.episodeOutputDirectory(episodeId, normalizedQuality);
    List<SegmentInfo> segments = buildSegmentList(hlsDir, episodeId, normalizedQuality);

    String offlineToken = offlineTokenService.generateOfflineToken(userId, episodeId,
        normalizedQuality);

    Date expiresAt = new Date(System.currentTimeMillis() + offlineTokenService.getOfflineTtlMs());

    OfflineMetadata metadata = new OfflineMetadata(movie.getId(), movie.getSlug(), movie.getTitle(),
        episodeId, episode.getTitle(), episode.getEpisodeNumber(),
        episode.getThumbnailUrl() != null ? episode.getThumbnailUrl() : movie.getPosterUrl(),
        episode.getDurationSeconds(), normalizedQuality);

    return new OfflinePackageResponse(offlineToken, expiresAt.toInstant().toString(), segments,
        metadata);
  }

  private List<SegmentInfo> buildSegmentList (Path hlsDir, Long episodeId, String quality) {
    List<SegmentInfo> segments = new ArrayList<>();
    Path playlistPath = hlsDir.resolve("master.m3u8");

    if (!Files.exists(playlistPath)) {
      throw new AppException(ErrorCode.FORBIDDEN);
    }

    try {
      List<String> lines = Files.readAllLines(playlistPath);
      String keyUrl = streamUrlService.offlineEpisodeKeyUrl(episodeId, quality);

      for (String line : lines) {
        line = line.trim();
        if (line.isEmpty() || line.startsWith("#")) {
          continue;
        }

        if (line.endsWith(".ts")) {
          String segmentUrl = streamUrlService.episodeSegmentUrl(episodeId, quality, line);
          segments.add(new SegmentInfo(segmentUrl, keyUrl));
        }
      }
    } catch (IOException e) {
      throw new AppException(ErrorCode.FORBIDDEN);
    }

    return segments;
  }

  public record SegmentInfo(String url, String keyUrl) {
  }

  public record OfflineMetadata(Long movieId, String movieSlug, String movieTitle, Long episodeId,
      String episodeTitle, Integer episodeNumber, String posterUrl, Integer durationSeconds,
      String quality) {
  }

  public record OfflinePackageResponse(String offlineToken, String expiresAt,
      List<SegmentInfo> segments, OfflineMetadata metadata) {
  }
}
