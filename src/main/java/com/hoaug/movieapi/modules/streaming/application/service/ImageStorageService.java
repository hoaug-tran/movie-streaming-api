package com.hoaug.movieapi.modules.streaming.application.service;

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
import com.hoaug.movieapi.modules.streaming.application.config.MediaStorageProperties;

@Component
public class ImageStorageService {

  private static final Set<String> ALLOWED_EXTENSIONS = Set.of(".jpg", ".jpeg", ".png", ".webp", ".gif");
  private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
      "image/jpeg", "image/png", "image/webp", "image/gif"
  );
  private static final long MAX_IMAGE_BYTES = 10 * 1024 * 1024L;

  private final MediaStorageProperties properties;

  public ImageStorageService (MediaStorageProperties properties) {
    this.properties = properties;
  }

  public String store (MultipartFile file) {
    validate(file);
    String ext = resolveExtension(file);
    String filename = UUID.randomUUID().toString() + ext;
    Path root = Path.of(properties.getImagesDirectory()).toAbsolutePath().normalize();
    Path destination = root.resolve(filename).normalize();

    if (!destination.startsWith(root)) {
      throw new AppException(ErrorCode.BAD_REQUEST);
    }

    try {
      Files.createDirectories(root);
      Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
      return filename;
    } catch (IOException e) {
      throw new AppException(ErrorCode.FILE_UPLOAD_FAILED);
    }
  }

  private void validate (MultipartFile file) {
    if (file == null || file.isEmpty() || file.getSize() > MAX_IMAGE_BYTES) {
      throw new AppException(ErrorCode.BAD_REQUEST);
    }
    String ext = resolveExtension(file);
    if (!ALLOWED_EXTENSIONS.contains(ext)) {
      throw new AppException(ErrorCode.BAD_REQUEST);
    }
    String contentType = file.getContentType();
    if (contentType != null && !ALLOWED_CONTENT_TYPES.contains(contentType.toLowerCase(Locale.ROOT))) {
      throw new AppException(ErrorCode.BAD_REQUEST);
    }
  }

  private String resolveExtension (MultipartFile file) {
    String name = file.getOriginalFilename() != null ? file.getOriginalFilename() : "";
    int dot = name.lastIndexOf('.');
    if (dot < 0) return ".jpg";
    return name.substring(dot).toLowerCase(Locale.ROOT);
  }
}
