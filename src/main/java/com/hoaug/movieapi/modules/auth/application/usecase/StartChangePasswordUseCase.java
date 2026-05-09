package com.hoaug.movieapi.modules.auth.application.usecase;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.auth.application.dto.request.StartChangePasswordRequest;
import com.hoaug.movieapi.modules.auth.application.dto.response.OtpChallengeResponse;
import com.hoaug.movieapi.modules.auth.application.service.AuthOtpService;
import com.hoaug.movieapi.modules.auth.domain.model.AuthOtpPurpose;
import com.hoaug.movieapi.modules.auth.domain.repository.AuthUserRepository;
import com.hoaug.movieapi.modules.user.domain.model.User;

@Component
public class StartChangePasswordUseCase {
  private final AuthUserRepository authUserRepository;
  private final PasswordEncoder passwordEncoder;
  private final AuthOtpService authOtpService;

  public StartChangePasswordUseCase(AuthUserRepository authUserRepository,
      PasswordEncoder passwordEncoder, AuthOtpService authOtpService) {
    this.authUserRepository = authUserRepository;
    this.passwordEncoder = passwordEncoder;
    this.authOtpService = authOtpService;
  }

  public OtpChallengeResponse execute (String username, StartChangePasswordRequest request) {
    User user = authUserRepository.findByUsername(username)
        .orElseThrow( () -> new AppException(ErrorCode.USER_NOT_FOUND));

    if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
      throw new AppException(ErrorCode.INVALID_CREDENTIALS);
    }

    return authOtpService.issue(AuthOtpPurpose.PASSWORD_CHANGE, user.getId(), user.getEmail(),
        user.getFullName(), null);
  }
}
