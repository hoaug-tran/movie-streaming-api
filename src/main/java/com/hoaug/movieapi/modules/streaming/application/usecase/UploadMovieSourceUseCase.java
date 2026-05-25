package com.hoaug.movieapi.modules.streaming.application.usecase;

import java.nio.file.Path;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.movie.domain.model.EpisodeStatus;
import com.hoaug.movieapi.modules.movie.infrastructure.persistence.entity.EpisodeEntity;
import com.hoaug.movieapi.modules.movie.infrastructure.persistence.entity.MovieEntity;
import com.hoaug.movieapi.modules.movie.infrastructure.persistence.repository.JpaEpisodeRepository;
import com.hoaug.movieapi.modules.movie.infrastructure.persistence.repository.JpaMovieRepository;
import com.hoaug.movieapi.modules.streaming.application.dto.response.MediaUploadResponse;
import com.hoaug.movieapi.modules.streaming.application.service.AsyncTranscodeService;
import com.hoaug.movieapi.modules.streaming.application.service.Mp4StorageService;
import com.hoaug.movieapi.modules.streaming.application.service.StreamUrlService;

@Component
public class UploadMovieSourceUseCase {

  private final JpaMovieRepository movieRepository;
  private final JpaEpisodeRepository episodeRepository;
  private final Mp4StorageService storageService;
  private final StreamUrlService streamUrlService;
  private final AsyncTranscodeService asyncTranscodeService;

  public UploadMovieSourceUseCase (JpaMovieRepository movieRepository,
      JpaEpisodeRepository episodeRepository, Mp4StorageService storageService,
      StreamUrlService streamUrlService, AsyncTranscodeService asyncTranscodeService) {
    this.movieRepository = movieRepository;
    this.episodeRepository = episodeRepository;
    this.storageService = storageService;
    this.streamUrlService = streamUrlService;
    this.asyncTranscodeService = asyncTranscodeService;
  }

  public MediaUploadResponse execute (Long movieId, MultipartFile file) {
    MovieEntity movie = movieRepository.findById(movieId)
        .orElseThrow(() -> new AppException(ErrorCode.MOVIE_NOT_FOUND));
    EpisodeEntity episode = findOrCreateMovieEpisode(movie);

    Path sourcePath = storageService.storeMovieSource(movieId, file);
    String mp4Url = streamUrlService.episodeMp4Url(episode.getId());
    episode.setVideoUrl(mp4Url);
    episode.setAvailableQualities("TRANSCODING");
    episodeRepository.save(episode);

    asyncTranscodeService.transcodeEpisodeAsync(episode.getId(), sourcePath);

    return new MediaUploadResponse(episode.getId(), mp4Url, "TRANSCODING");
  }

  private EpisodeEntity findOrCreateMovieEpisode (MovieEntity movie) {
    List<EpisodeEntity> episodes = episodeRepository.findByMovieIdOrderByEpisodeNumberAsc(movie.getId());
    if (!episodes.isEmpty()) {
      return episodes.get(0);
    }

    EpisodeEntity episode = new EpisodeEntity();
    episode.setMovieId(movie.getId());
    episode.setTitle(movie.getTitle());
    episode.setEpisodeNumber(1);
    episode.setVideoUrl("");
    episode.setDurationSeconds(0);
    episode.setIsFreePreview(false);
    episode.setStatus(EpisodeStatus.PUBLISHED);
    return episodeRepository.save(episode);
  }
}
