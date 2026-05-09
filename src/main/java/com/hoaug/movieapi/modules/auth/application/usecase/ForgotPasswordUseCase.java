package com.hoaug.movieapi.modules.auth.application.usecase;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.auth.application.dto.request.ForgotPasswordRequest;
import com.hoaug.movieapi.modules.auth.application.dto.response.OtpChallengeResponse;
import com.hoaug.movieapi.modules.auth.application.service.AuthOtpService;
import com.hoaug.movieapi.modules.auth.domain.model.AuthOtpPurpose;
import com.hoaug.movieapi.modules.auth.domain.repository.AuthUserRepository;
import com.hoaug.movieapi.modules.user.domain.model.User;

@Component
public class ForgotPasswordUseCase {
  private final AuthUserRepository authUserRepository;
  private final AuthOtpService authOtpService;

  public ForgotPasswordUseCase(AuthUserRepository authUserRepository,
      AuthOtpService authOtpService) {
    this.authUserRepository = authUserRepository;
    this.authOtpService = authOtpService;
  }

  public OtpChallengeResponse execute (ForgotPasswordRequest request) {
    User user = authUserRepository.findByEmail(request.getEmail()).orElse(null);

    if (user == null) {
      OtpChallengeResponse dummy = new OtpChallengeResponse();
      dummy.setOtpRequired(true);
      dummy.setMessage("Nếu email tồn tại, mã OTP đã được gửi đến email của bạn");
      dummy.setExpiresInSeconds(600);
      return dummy;
    }

    return authOtpService.issue(AuthOtpPurpose.PASSWORD_RESET, user.getId(), user.getEmail(),
        user.getFullName(), null);
  }
}
