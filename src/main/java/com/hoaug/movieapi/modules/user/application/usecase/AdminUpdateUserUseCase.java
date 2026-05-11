package com.hoaug.movieapi.modules.user.application.usecase;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.user.application.dto.request.AdminUpdateUserRequest;
import com.hoaug.movieapi.modules.user.application.dto.response.UserDetailResponse;
import com.hoaug.movieapi.modules.user.application.mapper.UserMapper;
import com.hoaug.movieapi.modules.user.domain.model.User;
import com.hoaug.movieapi.modules.user.domain.repository.UserRepository;

@Component
public class AdminUpdateUserUseCase {
  private final UserRepository userRepository;
  private final UserMapper userMapper;

  public AdminUpdateUserUseCase(UserRepository userRepository, UserMapper userMapper) {
    this.userRepository = userRepository;
    this.userMapper = userMapper;
  }

  public UserDetailResponse execute (Long userId, AdminUpdateUserRequest request) {
    User user = userRepository.findById(userId)
        .orElseThrow( () -> new AppException(ErrorCode.USER_NOT_FOUND));

    userRepository.findByEmail(request.getEmail())
        .filter(existing -> !existing.getId().equals(userId))
        .ifPresent(existing -> {
          throw new AppException(ErrorCode.EMAIL_EXISTED);
        });

    user.setEmail(request.getEmail());
    user.setFullName(request.getFullName());
    user.setAvatarUrl(request.getAvatarUrl());
    user.setRole(request.getRole());
    user.setAccountStatus(request.getAccountStatus());
    user.setPremiumExpiryDate(request.getPremiumExpiryDate());

    return userMapper.toDetailResponse(userRepository.save(user));
  }
}
