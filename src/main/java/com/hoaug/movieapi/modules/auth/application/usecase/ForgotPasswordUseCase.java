package com.hoaug.movieapi.modules.auth.application.usecase;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.auth.application.dto.request.ForgotPasswordRequest;
import com.hoaug.movieapi.modules.auth.domain.model.PasswordResetToken;
import com.hoaug.movieapi.modules.auth.domain.repository.AuthUserRepository;
import com.hoaug.movieapi.modules.auth.domain.repository.PasswordResetTokenRepository;
import com.hoaug.movieapi.modules.auth.domain.service.TokenService;
import com.hoaug.movieapi.modules.user.domain.model.User;

@Component
public class ForgotPasswordUseCase {

  private final AuthUserRepository authUserRepository;
  private final PasswordResetTokenRepository passwordResetTokenRepository;
  private final TokenService tokenService;

  public ForgotPasswordUseCase(AuthUserRepository authUserRepository,
      PasswordResetTokenRepository passwordResetTokenRepository, TokenService tokenService) {
    this.authUserRepository = authUserRepository;
    this.passwordResetTokenRepository = passwordResetTokenRepository;
    this.tokenService = tokenService;
  }

  public String execute (ForgotPasswordRequest request) {
    User user = authUserRepository.findByEmail(request.getEmail()).orElse(null);

    if (user != null) {
      PasswordResetToken token = new PasswordResetToken();
      token.setUserId(user.getId());
      token.setToken(tokenService.generateRefreshToken());
      token.setExpiresAt(LocalDateTime.now().plusMinutes(15));
      token.setCreatedAt(LocalDateTime.now());
      passwordResetTokenRepository.save(token);

      // TODO: GỬI MAIL
      // TODO: sendEmail(user.getEmail(), token.getToken())
    }
    return "Nếu email tồn tại, một liên kết đặt lại mật khẩu đã được gửi";
  }
}
