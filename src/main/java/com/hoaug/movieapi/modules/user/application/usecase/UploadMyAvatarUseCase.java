package com.hoaug.movieapi.modules.user.application.usecase;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import org.springframework.util.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.user.application.config.AvatarStorageProperties;
import com.hoaug.movieapi.modules.user.application.dto.response.UserProfileResponse;
import com.hoaug.movieapi.modules.user.application.mapper.UserMapper;
import com.hoaug.movieapi.modules.user.domain.model.User;
import com.hoaug.movieapi.modules.user.domain.repository.UserRepository;

@Component
public class UploadMyAvatarUseCase {
  private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of("image/jpeg", "image/png", "image/webp");

  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final AvatarStorageProperties properties;

  public UploadMyAvatarUseCase(UserRepository userRepository, UserMapper userMapper,
      AvatarStorageProperties properties) {
    this.userRepository = userRepository;
    this.userMapper = userMapper;
    this.properties = properties;
  }

  public UserProfileResponse execute (String username, MultipartFile avatar) {
    if (!StringUtils.hasText(properties.getDirectory()) || !StringUtils.hasText(properties.getPublicBaseUrl())) {
      throw new AppException(ErrorCode.BAD_REQUEST);
    }

    if (avatar == null || avatar.isEmpty()) {
      throw new AppException(ErrorCode.BAD_REQUEST);
    }

    String contentType = avatar.getContentType();
    if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType.toLowerCase(Locale.ROOT))) {
      throw new AppException(ErrorCode.BAD_REQUEST);
    }

    if (avatar.getSize() > properties.getMaxBytes()) {
      throw new AppException(ErrorCode.BAD_REQUEST);
    }

    User user = userRepository.findByUsername(username)
        .orElseThrow( () -> new AppException(ErrorCode.USER_NOT_FOUND));

    String filename = "user-" + user.getId() + "-" + UUID.randomUUID() + extensionFor(contentType);
    Path directory = Paths.get(properties.getDirectory()).toAbsolutePath().normalize();
    Path destination = directory.resolve(filename).normalize();

    try {
      Files.createDirectories(directory);
      Files.copy(avatar.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
    } catch (IOException exception) {
      throw new AppException(ErrorCode.FILE_UPLOAD_FAILED);
    }

    String publicUrl = properties.getPublicBaseUrl().replaceAll("/+$", "") + "/" + filename;
    user.setAvatarUrl(publicUrl);
    user.setProfilePictureUrl(publicUrl);

    return userMapper.toProfileResponse(userRepository.save(user));
  }

  private String extensionFor (String contentType) {
    return switch (contentType.toLowerCase(Locale.ROOT)) {
      case "image/png" -> ".png";
      case "image/webp" -> ".webp";
      default -> ".jpg";
    };
  }
}
