package com.hoaug.movieapi.modules.user.application.usecase;

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.email.application.EmailService;
import com.hoaug.movieapi.modules.user.domain.model.User;
import com.hoaug.movieapi.modules.user.domain.repository.UserRepository;



@Component
public class DeleteUserUseCase {

  private final UserRepository userRepository;
  private final EmailService emailService;

  public DeleteUserUseCase(UserRepository userRepository, EmailService emailService) {
    this.userRepository = userRepository;
    this.emailService = emailService;
  }

  public void execute (Long userId) {
    User user = userRepository.findById(userId)
        .orElseThrow( () -> new AppException(ErrorCode.USER_NOT_FOUND));

    try {
      emailService.sendAccountNotificationEmail(user.getEmail(), user.getFullName(),
          "Tài khoản của bạn đã được xóa. Nếu bạn muốn khôi phục, vui lòng liên hệ với chúng tôi trong vòng 30 ngày.");
    } catch (IOException e) {
      org.slf4j.LoggerFactory.getLogger(this.getClass())
          .warn("Failed to send account deletion notification", e);
    }

    userRepository.deleteById(userId);
  }
}
