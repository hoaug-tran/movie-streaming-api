package com.hoaug.movieapi.modules.streaming.application.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import java.util.Set;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.streaming.application.config.MediaStorageProperties;

@Component
public class Mp4StorageService {

  private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
      "video/mp4", "application/mp4",
      "video/x-matroska", "video/mkv",
      "video/avi", "video/x-msvideo", "video/x-avi",
      "video/quicktime",
      "video/webm",
      "video/x-ms-wmv", "video/wmv",
      "video/mpeg", "video/mpg", "video/x-mpeg",
      "video/x-flv", "video/flv",
      "video/3gpp", "video/3gpp2",
      "video/ogg",
      "video/x-ms-asf",
      "application/octet-stream" // some browsers send this for mkv/avi
  );

  private static final Set<String> ALLOWED_EXTENSIONS = Set.of(
      ".mp4", ".m4v",
      ".mkv",
      ".avi",
      ".mov",
      ".webm",
      ".wmv",
      ".mpg", ".mpeg",
      ".flv",
      ".3gp", ".3g2",
      ".ogv",
      ".ts", ".m2ts"
  );

  private final MediaStorageProperties properties;

  public Mp4StorageService(MediaStorageProperties properties) {
    this.properties = properties;
  }

  public Path storeEpisodeSource (Long episodeId, MultipartFile file) {
    return store(file, Path.of(properties.getSeriesDataDirectory()), "episodes", episodeId);
  }

  public Path storeMovieSource (Long movieId, MultipartFile file) {
    return store(file, Path.of(properties.getMoviesDataDirectory()), null, movieId);
  }

  public Path storeAdvertisementSource (Long advertisementId, MultipartFile file) {
    return store(file, Path.of(properties.getAdsDataDirectory()), "advertisements", advertisementId);
  }

  public Path resolveEpisodeDestination (Long episodeId, String originalFileName) {
    return resolveDestination(Path.of(properties.getSeriesDataDirectory()), "episodes", episodeId,
        originalFileName);
  }

  public Path resolveMovieDestination (Long movieId, String originalFileName) {
    return resolveDestination(Path.of(properties.getMoviesDataDirectory()), null, movieId,
        originalFileName);
  }

  public Path resolveAdvertisementDestination (Long advertisementId, String originalFileName) {
    return resolveDestination(Path.of(properties.getAdsDataDirectory()), "advertisements",
        advertisementId, originalFileName);
  }

  private Path resolveDestination (Path rootDirectory, String typeDirectory, Long id,
      String originalFileName) {
    String ext = extensionOf(originalFileName);
    if (!ALLOWED_EXTENSIONS.contains(ext)) {
      throw new AppException(ErrorCode.BAD_REQUEST);
    }
    Path root = rootDirectory.toAbsolutePath().normalize();
    Path destinationDirectory = typeDirectory != null
        ? root.resolve(typeDirectory).resolve(String.valueOf(id)).normalize()
        : root.resolve(String.valueOf(id)).normalize();
    Path destination = destinationDirectory.resolve("source" + ext).normalize();
    if (!destination.startsWith(root)) {
      throw new AppException(ErrorCode.BAD_REQUEST);
    }
    return destination;
  }

  private String extensionOf (String originalFileName) {
    String name = originalFileName != null ? originalFileName : "";
    int dot = name.lastIndexOf('.');
    if (dot < 0) return ".mp4";
    return name.substring(dot).toLowerCase(Locale.ROOT);
  }

  private Path store (MultipartFile file, Path rootDirectory, String typeDirectory, Long id) {
    validate(file);
    String ext = resolveExtension(file);
    Path root = rootDirectory.toAbsolutePath().normalize();
    Path destinationDirectory = typeDirectory != null
        ? root.resolve(typeDirectory).resolve(String.valueOf(id)).normalize()
        : root.resolve(String.valueOf(id)).normalize();
    Path destination = destinationDirectory.resolve("source" + ext).normalize();

    if (!destination.startsWith(root)) {
      throw new AppException(ErrorCode.BAD_REQUEST);
    }

    try {
      Files.createDirectories(destinationDirectory);
      Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
      return destination;
    } catch (IOException exception) {
      throw new AppException(ErrorCode.FILE_UPLOAD_FAILED);
    }
  }

  private void validate (MultipartFile file) {
    if (file == null || file.isEmpty() || file.getSize() > properties.getMaxUploadBytes()) {
      throw new AppException(ErrorCode.BAD_REQUEST);
    }

    String ext = resolveExtension(file);
    if (!ALLOWED_EXTENSIONS.contains(ext)) {
      throw new AppException(ErrorCode.BAD_REQUEST);
    }

    String contentType = file.getContentType();
    if (contentType != null
        && !contentType.startsWith("video/")
        && !ALLOWED_CONTENT_TYPES.contains(contentType.toLowerCase(Locale.ROOT))) {
      throw new AppException(ErrorCode.BAD_REQUEST);
    }
  }

  private String resolveExtension (MultipartFile file) {
    String originalFilename = StringUtils.cleanPath(
        file.getOriginalFilename() != null ? file.getOriginalFilename() : "");
    int dotIndex = originalFilename.lastIndexOf('.');
    if (dotIndex < 0) return ".mp4";
    return originalFilename.substring(dotIndex).toLowerCase(Locale.ROOT);
  }
}
