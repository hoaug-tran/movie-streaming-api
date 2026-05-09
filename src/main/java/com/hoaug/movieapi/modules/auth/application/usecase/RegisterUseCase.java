package com.hoaug.movieapi.modules.auth.application.usecase;

import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.auth.application.dto.request.RegisterRequest;
import com.hoaug.movieapi.modules.auth.application.dto.response.OtpChallengeResponse;
import com.hoaug.movieapi.modules.auth.application.service.AuthOtpService;
import com.hoaug.movieapi.modules.auth.domain.model.AuthOtpPurpose;
import com.hoaug.movieapi.modules.auth.domain.repository.AuthUserRepository;
import com.hoaug.movieapi.modules.user.domain.model.AccountStatus;
import com.hoaug.movieapi.modules.user.domain.model.Role;
import com.hoaug.movieapi.modules.user.domain.model.User;

@Component
public class RegisterUseCase {
  private final AuthUserRepository authUserRepository;
  private final PasswordEncoder passwordEncoder;
  private final AuthOtpService authOtpService;

  public RegisterUseCase(AuthUserRepository authUserRepository, PasswordEncoder passwordEncoder,
      AuthOtpService authOtpService) {
    this.authUserRepository = authUserRepository;
    this.passwordEncoder = passwordEncoder;
    this.authOtpService = authOtpService;
  }

  public OtpChallengeResponse execute (RegisterRequest request) {
    if (authUserRepository.existsByUsername(request.getUsername())) {
      throw new AppException(ErrorCode.USER_EXISTED);
    }

    if (authUserRepository.existsByEmail(request.getEmail())) {
      throw new AppException(ErrorCode.EMAIL_EXISTED);
    }

    User user = new User();
    user.setUsername(request.getUsername());
    user.setEmail(request.getEmail());
    user.setPassword(passwordEncoder.encode(request.getPassword()));
    user.setFullName(request.getFullName());
    user.setAvatarUrl(request.getAvatarUrl());
    user.setRole(Role.ROLE_USER);
    user.setAccountStatus(AccountStatus.PENDING);
    user.setCreatedAt(LocalDateTime.now());
    user.setUpdatedAt(LocalDateTime.now());

    User savedUser = authUserRepository.save(user);

    return authOtpService.issue(AuthOtpPurpose.REGISTER, savedUser.getId(), savedUser.getEmail(),
        savedUser.getFullName(), null);
  }
}
