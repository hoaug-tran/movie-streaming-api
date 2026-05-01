package com.hoaug.movieapi.modules.user.application.usecase;

import java.security.SecureRandom;
import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.email.application.EmailService;
import com.hoaug.movieapi.modules.user.application.dto.request.VerifyEmailChangeOtpRequest;
import com.hoaug.movieapi.modules.user.application.dto.response.EmailChangeResponse;
import com.hoaug.movieapi.modules.user.application.service.EmailChangeSessionStore;
import com.hoaug.movieapi.modules.user.application.service.EmailChangeSessionStore.EmailChangeSession;
import com.hoaug.movieapi.modules.user.domain.model.EmailChangeStatus;
import com.hoaug.movieapi.modules.user.domain.model.User;
import com.hoaug.movieapi.modules.user.domain.repository.UserRepository;

@Component
public class VerifyCurrentEmailChangeUseCase {
  private final UserRepository userRepository;
  private final EmailChangeSessionStore emailChangeSessionStore;
  private final PasswordEncoder passwordEncoder;
  private final EmailService emailService;
  private final SecureRandom secureRandom = new SecureRandom();

  public VerifyCurrentEmailChangeUseCase(UserRepository userRepository,
      EmailChangeSessionStore emailChangeSessionStore, PasswordEncoder passwordEncoder,
      EmailService emailService) {
    this.userRepository = userRepository;
    this.emailChangeSessionStore = emailChangeSessionStore;
    this.passwordEncoder = passwordEncoder;
    this.emailService = emailService;
  }

  public EmailChangeResponse execute (String username, VerifyEmailChangeOtpRequest request) {
    User user = userRepository.findByUsername(username)
        .orElseThrow( () -> new AppException(ErrorCode.USER_NOT_FOUND));
    EmailChangeSession session = emailChangeSessionStore.find(user.getId(), EmailChangeStatus.PENDING_CURRENT)
        .orElseThrow( () -> new AppException(ErrorCode.VALIDATION_ERROR));
    assertValid(session);
    if (!passwordEncoder.matches(request.getOtp(), session.getCurrentEmailOtpHash())) {
      session.setAttemptCount(session.getAttemptCount() + 1);
      emailChangeSessionStore.save(session);
      throw new AppException(ErrorCode.VALIDATION_ERROR);
    }
    String newOtp = generateOtp();
    session.setNewEmailOtpHash(passwordEncoder.encode(newOtp));
    session.setStatus(EmailChangeStatus.PENDING_NEW);
    emailChangeSessionStore.save(session);
    sendOtp(session.getNewEmail(), user.getFullName(), newOtp);
    return toResponse(session);
  }

  private void assertValid (EmailChangeSession session) {
    if (session.getExpiresAt().isBefore(LocalDateTime.now()) || session.getAttemptCount() >= 5) {
      emailChangeSessionStore.remove(session.getUserId());
      throw new AppException(ErrorCode.VALIDATION_ERROR);
    }
  }

  private String generateOtp () {
    return String.format("%06d", secureRandom.nextInt(1_000_000));
  }

  private void sendOtp (String email, String fullName, String otp) {
    try {
      emailService.sendAccountNotificationEmail(email, fullName,
          "Mã OTP để xác minh email mới của bạn là " + otp + ". Mã có hiệu lực trong 10 phút.");
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
