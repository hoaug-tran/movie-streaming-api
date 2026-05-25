package com.hoaug.movieapi.modules.user.application.usecase;

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.email.application.EmailService;
import com.hoaug.movieapi.modules.user.application.dto.request.UpdateUserStatusRequest;
import com.hoaug.movieapi.modules.user.application.dto.response.UserDetailResponse;
import com.hoaug.movieapi.modules.user.application.mapper.UserMapper;
import com.hoaug.movieapi.modules.user.domain.model.User;
import com.hoaug.movieapi.modules.user.domain.repository.UserRepository;



@Component
public class UpdateUserStatusUseCase {
  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final EmailService emailService;

  public UpdateUserStatusUseCase(UserRepository userRepository, UserMapper userMapper,
      EmailService emailService) {
    this.userRepository = userRepository;
    this.userMapper = userMapper;
    this.emailService = emailService;
  }

  public UserDetailResponse execute (Long userId, UpdateUserStatusRequest request) {
    User user = userRepository.findById(userId)
        .orElseThrow( () -> new AppException(ErrorCode.USER_NOT_FOUND));

    user.setAccountStatus(request.getAccountStatus());

    User savedUser = userRepository.save(user);

    try {
      String notification = "Trạng thái tài khoản của bạn đã được thay đổi thành: "
          + request.getAccountStatus();
      emailService.sendAccountNotificationEmail(savedUser.getEmail(), savedUser.getFullName(),
          notification);
    } catch (IOException e) {
      org.slf4j.LoggerFactory.getLogger(this.getClass())
          .warn("Failed to send account status change notification", e);
    }

    return userMapper.toDetailResponse(savedUser);
  }
}
