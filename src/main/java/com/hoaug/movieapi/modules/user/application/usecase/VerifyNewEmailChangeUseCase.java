package com.hoaug.movieapi.modules.user.application.usecase;

import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.user.application.dto.request.VerifyEmailChangeOtpRequest;
import com.hoaug.movieapi.modules.user.application.dto.response.EmailChangeResponse;
import com.hoaug.movieapi.modules.user.application.service.EmailChangeSessionStore;
import com.hoaug.movieapi.modules.user.application.service.EmailChangeSessionStore.EmailChangeSession;
import com.hoaug.movieapi.modules.user.domain.model.EmailChangeStatus;
import com.hoaug.movieapi.modules.user.domain.model.User;
import com.hoaug.movieapi.modules.user.domain.repository.UserRepository;

@Component
public class VerifyNewEmailChangeUseCase {
  private final UserRepository userRepository;
  private final EmailChangeSessionStore emailChangeSessionStore;
  private final PasswordEncoder passwordEncoder;

  public VerifyNewEmailChangeUseCase(UserRepository userRepository,
      EmailChangeSessionStore emailChangeSessionStore, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.emailChangeSessionStore = emailChangeSessionStore;
    this.passwordEncoder = passwordEncoder;
  }

  public EmailChangeResponse execute (String username, VerifyEmailChangeOtpRequest request) {
    User user = userRepository.findByUsername(username)
        .orElseThrow( () -> new AppException(ErrorCode.USER_NOT_FOUND));
    EmailChangeSession session = emailChangeSessionStore.find(user.getId(), EmailChangeStatus.PENDING_NEW)
        .orElseThrow( () -> new AppException(ErrorCode.VALIDATION_ERROR));
    assertValid(session);
    if (!passwordEncoder.matches(request.getOtp(), session.getNewEmailOtpHash())) {
      session.setAttemptCount(session.getAttemptCount() + 1);
      emailChangeSessionStore.save(session);
      throw new AppException(ErrorCode.VALIDATION_ERROR);
    }
    if (userRepository.existsByEmail(session.getNewEmail())) {
      throw new AppException(ErrorCode.EMAIL_EXISTED);
    }
    user.setEmail(session.getNewEmail());
    userRepository.save(user);
    session.setStatus(EmailChangeStatus.VERIFIED);
    emailChangeSessionStore.remove(user.getId());
    return toResponse(session);
  }

  private void assertValid (EmailChangeSession session) {
    if (session.getExpiresAt().isBefore(LocalDateTime.now()) || session.getAttemptCount() >= 5) {
      emailChangeSessionStore.remove(session.getUserId());
      throw new AppException(ErrorCode.VALIDATION_ERROR);
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
