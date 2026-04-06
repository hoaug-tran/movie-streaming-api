package com.hoaug.movieapi.modules.user.application.usecase;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.user.application.dto.request.UpdateUserProfileRequest;
import com.hoaug.movieapi.modules.user.application.dto.response.UserProfileResponse;
import com.hoaug.movieapi.modules.user.application.mapper.UserMapper;
import com.hoaug.movieapi.modules.user.domain.model.User;
import com.hoaug.movieapi.modules.user.domain.repository.UserRepository;

@Component
public class UpdateMyProfileUseCase {
  private final UserRepository userRepository;
  private final UserMapper userMapper;

  public UpdateMyProfileUseCase(UserRepository userRepository, UserMapper userMapper) {
    this.userRepository = userRepository;
    this.userMapper = userMapper;
  }

  public UserProfileResponse execute (String username, UpdateUserProfileRequest request) {
    User user = userRepository.findByUsername(username)
        .orElseThrow( () -> new AppException(ErrorCode.USER_NOT_FOUND));

    user.setFullName(request.getFullName());
    user.setAvatarUrl(request.getAvatarUrl());

    if (!user.getEmail().equals(request.getEmail())) {
      if (userRepository.existsByEmail(request.getEmail())) {
        throw new AppException(ErrorCode.EMAIL_EXISTED);
      }
    }

    user.setEmail(request.getEmail());

    User savedUser = userRepository.save(user);

    return userMapper.toProfileResponse(savedUser);
  }
}
