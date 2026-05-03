package com.hoaug.movieapi.modules.streaming.application.service;

import java.io.IOException;
import java.io.InputStream;
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
  private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of("video/mp4", "application/mp4");
  private static final byte[] MP4_SIGNATURE = new byte[] { 0x66, 0x74, 0x79, 0x70 };
  private final MediaStorageProperties properties;

  public Mp4StorageService(MediaStorageProperties properties) {
    this.properties = properties;
  }

  public Path storeEpisodeSource (Long episodeId, MultipartFile file) {
    return store(file, Path.of(properties.getSeriesDataDirectory()), "episodes", episodeId);
  }

  public Path storeAdvertisementSource (Long advertisementId, MultipartFile file) {
    return store(file, Path.of(properties.getAdsDataDirectory()), "advertisements", advertisementId);
  }

  private Path store (MultipartFile file, Path rootDirectory, String typeDirectory, Long id) {
    validate(file);
    Path root = rootDirectory.toAbsolutePath().normalize();
    Path destinationDirectory = root.resolve(typeDirectory).resolve(String.valueOf(id)).normalize();
    Path destination = destinationDirectory.resolve("source.mp4").normalize();

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

    String contentType = file.getContentType();
    if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType.toLowerCase(Locale.ROOT))) {
      throw new AppException(ErrorCode.BAD_REQUEST);
    }

    String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
    if (!StringUtils.hasText(originalFilename) || !originalFilename.toLowerCase(Locale.ROOT).endsWith(".mp4")) {
      throw new AppException(ErrorCode.BAD_REQUEST);
    }

    byte[] header = new byte[12];
    try (InputStream inputStream = file.getInputStream()) {
      int bytesRead = inputStream.read(header);
      if (bytesRead < 12 || !matchesMp4Signature(header)) {
        throw new AppException(ErrorCode.BAD_REQUEST);
      }
    } catch (IOException exception) {
      throw new AppException(ErrorCode.BAD_REQUEST);
    }
  }

  private boolean matchesMp4Signature (byte[] header) {
    for (int index = 0; index < MP4_SIGNATURE.length; index++) {
      if (header[index + 4] != MP4_SIGNATURE[index]) {
        return false;
      }
    }
    return true;
  }
}
