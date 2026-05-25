package com.hoaug.movieapi.modules.streaming.application.usecase;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.movie.infrastructure.persistence.entity.EpisodeEntity;
import com.hoaug.movieapi.modules.movie.infrastructure.persistence.entity.MovieEntity;
import com.hoaug.movieapi.modules.movie.infrastructure.persistence.repository.JpaEpisodeRepository;
import com.hoaug.movieapi.modules.movie.infrastructure.persistence.repository.JpaMovieRepository;
import com.hoaug.movieapi.modules.streaming.application.config.MediaStorageProperties;
import com.hoaug.movieapi.modules.streaming.application.dto.response.MediaUploadResponse;
import com.hoaug.movieapi.modules.streaming.application.service.StreamUrlService;

@Component
public class UploadSeriesTrailerUseCase {

  private static final Set<String> ALLOWED_EXTENSIONS = Set.of(
      ".mp4", ".m4v", ".mkv", ".avi", ".mov", ".webm", ".wmv", ".mpg", ".mpeg", ".flv"
  );
  private static final long MAX_TRAILER_BYTES = 500L * 1024 * 1024;

  private final JpaEpisodeRepository episodeRepository;
  private final JpaMovieRepository movieRepository;
  private final MediaStorageProperties properties;
  private final StreamUrlService streamUrlService;

  public UploadSeriesTrailerUseCase(JpaEpisodeRepository episodeRepository,
      JpaMovieRepository movieRepository, MediaStorageProperties properties,
      StreamUrlService streamUrlService) {
    this.episodeRepository = episodeRepository;
    this.movieRepository = movieRepository;
    this.properties = properties;
    this.streamUrlService = streamUrlService;
  }

  public MediaUploadResponse execute(Long episodeId, MultipartFile file) {
    EpisodeEntity episode = episodeRepository.findById(episodeId)
        .orElseThrow(() -> new AppException(ErrorCode.EPISODE_NOT_FOUND));
    MovieEntity movie = movieRepository.findById(episode.getMovieId())
        .orElseThrow(() -> new AppException(ErrorCode.MOVIE_NOT_FOUND));

    validate(file);
    String ext = resolveExtension(file);
    String filename = UUID.randomUUID().toString() + ext;
    Path root = Path.of(properties.getSeriesDataDirectory(), "episodes", episodeId.toString()).toAbsolutePath().normalize();
    Path destination = root.resolve(filename).normalize();

    if (!destination.startsWith(root)) {
      throw new AppException(ErrorCode.BAD_REQUEST);
    }

    try {
      Files.createDirectories(root);
      Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
    } catch (IOException e) {
      throw new AppException(ErrorCode.FILE_UPLOAD_FAILED);
    }

    String trailerUrl = streamUrlService.seriesTrailerUrl(episodeId, filename);
    movie.setTrailerUrl(trailerUrl);
    movieRepository.save(movie);

    return new MediaUploadResponse(movie.getId(), trailerUrl, "UPLOADED");
  }

  private void validate(MultipartFile file) {
    if (file == null || file.isEmpty() || file.getSize() > MAX_TRAILER_BYTES) {
      throw new AppException(ErrorCode.BAD_REQUEST);
    }
    String ext = resolveExtension(file);
    if (!ALLOWED_EXTENSIONS.contains(ext)) {
      throw new AppException(ErrorCode.BAD_REQUEST);
    }
  }

  private String resolveExtension(MultipartFile file) {
    String name = file.getOriginalFilename() != null ? file.getOriginalFilename() : "";
    int dot = name.lastIndexOf('.');
    if (dot < 0) return ".mp4";
    return name.substring(dot).toLowerCase(Locale.ROOT);
  }
}
