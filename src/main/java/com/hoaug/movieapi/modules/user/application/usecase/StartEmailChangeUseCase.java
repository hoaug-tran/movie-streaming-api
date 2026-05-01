package com.hoaug.movieapi.modules.user.application.usecase;

import java.security.SecureRandom;
import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.email.application.EmailService;
import com.hoaug.movieapi.modules.user.application.dto.request.StartEmailChangeRequest;
import com.hoaug.movieapi.modules.user.application.dto.response.EmailChangeResponse;
import com.hoaug.movieapi.modules.user.application.service.EmailChangeSessionStore;
import com.hoaug.movieapi.modules.user.application.service.EmailChangeSessionStore.EmailChangeSession;
import com.hoaug.movieapi.modules.user.domain.model.EmailChangeStatus;
import com.hoaug.movieapi.modules.user.domain.model.User;
import com.hoaug.movieapi.modules.user.domain.repository.UserRepository;

@Component
public class StartEmailChangeUseCase {
  private final UserRepository userRepository;
  private final EmailChangeSessionStore emailChangeSessionStore;
  private final PasswordEncoder passwordEncoder;
  private final EmailService emailService;
  private final SecureRandom secureRandom = new SecureRandom();

  public StartEmailChangeUseCase(UserRepository userRepository,
      EmailChangeSessionStore emailChangeSessionStore, PasswordEncoder passwordEncoder,
      EmailService emailService) {
    this.userRepository = userRepository;
    this.emailChangeSessionStore = emailChangeSessionStore;
    this.passwordEncoder = passwordEncoder;
    this.emailService = emailService;
  }

  public EmailChangeResponse execute (String username, StartEmailChangeRequest request) {
    User user = userRepository.findByUsername(username)
        .orElseThrow( () -> new AppException(ErrorCode.USER_NOT_FOUND));
    String newEmail = request.getNewEmail().trim().toLowerCase();
    if (user.getEmail().equalsIgnoreCase(newEmail) || userRepository.existsByEmail(newEmail)) {
      throw new AppException(ErrorCode.EMAIL_EXISTED);
    }

    String otp = generateOtp();
    EmailChangeSession session = new EmailChangeSession();
    session.setUserId(user.getId());
    session.setCurrentEmail(user.getEmail());
    session.setNewEmail(newEmail);
    session.setCurrentEmailOtpHash(passwordEncoder.encode(otp));
    session.setExpiresAt(LocalDateTime.now().plusMinutes(10));
    session.setAttemptCount(0);
    session.setStatus(EmailChangeStatus.PENDING_CURRENT);
    emailChangeSessionStore.save(session);
    sendOtp(user.getEmail(), user.getFullName(), otp, "xác minh email hiện tại");
    return toResponse(session);
  }

  private String generateOtp () {
    return String.format("%06d", secureRandom.nextInt(1_000_000));
  }

  private void sendOtp (String email, String fullName, String otp, String purpose) {
    try {
      emailService.sendAccountNotificationEmail(email, fullName,
          "Mã OTP để " + purpose + " của bạn là " + otp + ". Mã có hiệu lực trong 10 phút.");
    } catch (Exception ignored) {
    }
  }

  private EmailChangeResponse toResponse (EmailChangeSession session) {
    EmailChangeResponse response = new EmailChangeResponse();
    response.setCurrentEmail(session.getCurrentEmail());
    response.setNewEmail(session.getNewEmail());
    response.setStatus(session.getStatus());
    response.setExpiresAt(session.getExpiresAt());
    return response;
  }
}
