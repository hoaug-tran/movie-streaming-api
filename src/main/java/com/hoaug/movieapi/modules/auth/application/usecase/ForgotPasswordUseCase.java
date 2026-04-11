package com.hoaug.movieapi.modules.auth.application.usecase;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.auth.application.dto.request.ForgotPasswordRequest;
import com.hoaug.movieapi.modules.auth.domain.model.PasswordResetToken;
import com.hoaug.movieapi.modules.auth.domain.repository.AuthUserRepository;
import com.hoaug.movieapi.modules.auth.domain.repository.PasswordResetTokenRepository;
import com.hoaug.movieapi.modules.auth.domain.service.TokenService;
import com.hoaug.movieapi.modules.email.application.EmailService;
import com.hoaug.movieapi.modules.user.domain.model.User;

import jakarta.mail.MessagingException;

@Component
public class ForgotPasswordUseCase {

  private final AuthUserRepository authUserRepository;
  private final PasswordResetTokenRepository passwordResetTokenRepository;
  private final TokenService tokenService;
  private final EmailService emailService;

  @Value("${app.url.reset-password}")
  private String resetPasswordUrl;

  public ForgotPasswordUseCase(AuthUserRepository authUserRepository,
      PasswordResetTokenRepository passwordResetTokenRepository, TokenService tokenService,
      EmailService emailService) {
    this.authUserRepository = authUserRepository;
    this.passwordResetTokenRepository = passwordResetTokenRepository;
    this.tokenService = tokenService;
    this.emailService = emailService;
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

      try {
        String resetLink = resetPasswordUrl + token.getToken();
        emailService.sendForgotPasswordEmail(user.getEmail(), resetLink, user.getFullName());
      } catch (MessagingException | UnsupportedEncodingException e) {
        org.slf4j.LoggerFactory.getLogger(this.getClass())
            .warn("Failed to send forgot password email", e);
      }
    }
    return "Nếu email tồn tại, một liên kết đặt lại mật khẩu đã được gửi";
  }
}
