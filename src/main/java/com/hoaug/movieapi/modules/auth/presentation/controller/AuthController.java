package com.hoaug.movieapi.modules.auth.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hoaug.movieapi.common.response.ResponseUtil;
import com.hoaug.movieapi.modules.auth.application.dto.request.ChangePasswordRequest;
import com.hoaug.movieapi.modules.auth.application.dto.request.ForgotPasswordRequest;
import com.hoaug.movieapi.modules.auth.application.dto.request.LoginRequest;
import com.hoaug.movieapi.modules.auth.application.dto.request.RefreshTokenRequest;
import com.hoaug.movieapi.modules.auth.application.dto.request.RegisterRequest;
import com.hoaug.movieapi.modules.auth.application.dto.request.ResetPasswordRequest;
import com.hoaug.movieapi.modules.auth.application.dto.response.AuthResponse;
import com.hoaug.movieapi.modules.auth.application.dto.response.CurrentUserResponse;
import com.hoaug.movieapi.modules.auth.application.dto.response.MessageResponse;
import com.hoaug.movieapi.modules.auth.application.dto.response.RefreshTokenResponse;
import com.hoaug.movieapi.modules.auth.application.usecase.ChangePasswordUseCase;
import com.hoaug.movieapi.modules.auth.application.usecase.ForgotPasswordUseCase;
import com.hoaug.movieapi.modules.auth.application.usecase.GetCurrentUserUseCase;
import com.hoaug.movieapi.modules.auth.application.usecase.LoginUseCase;
import com.hoaug.movieapi.modules.auth.application.usecase.LogoutUseCase;
import com.hoaug.movieapi.modules.auth.application.usecase.RefreshTokenUseCase;
import com.hoaug.movieapi.modules.auth.application.usecase.RegisterUseCase;
import com.hoaug.movieapi.modules.auth.application.usecase.ResetPasswordUseCase;

import jakarta.validation.Valid;

@RestController
@RequestMapping("${api.prefix:/api/v1}/auth")
public class AuthController {

  private final RegisterUseCase registerUseCase;
  private final LoginUseCase loginUseCase;
  private final GetCurrentUserUseCase getCurrentUserUseCase;
  private final RefreshTokenUseCase refreshTokenUseCase;
  private final LogoutUseCase logoutUseCase;
  private final ForgotPasswordUseCase forgotPasswordUseCase;
  private final ResetPasswordUseCase resetPasswordUseCase;
  private final ChangePasswordUseCase changePasswordUseCase;

  public AuthController(RegisterUseCase registerUseCase, LoginUseCase loginUseCase,
      GetCurrentUserUseCase getCurrentUserUseCase, RefreshTokenUseCase refreshTokenUseCase,
      LogoutUseCase logoutUseCase, ForgotPasswordUseCase forgotPasswordUseCase,
      ResetPasswordUseCase resetPasswordUseCase, ChangePasswordUseCase changePasswordUseCase) {
    this.registerUseCase = registerUseCase;
    this.loginUseCase = loginUseCase;
    this.getCurrentUserUseCase = getCurrentUserUseCase;
    this.refreshTokenUseCase = refreshTokenUseCase;
    this.logoutUseCase = logoutUseCase;
    this.forgotPasswordUseCase = forgotPasswordUseCase;
    this.resetPasswordUseCase = resetPasswordUseCase;
    this.changePasswordUseCase = changePasswordUseCase;
  }

  @PostMapping("/register")
  public ResponseEntity<AuthResponse> register (@Valid @RequestBody RegisterRequest request) {
    AuthResponse response = registerUseCase.execute(request);
    return ResponseUtil.created(response);
  }

  @PostMapping("/login")
  public ResponseEntity<AuthResponse> login (@Valid @RequestBody LoginRequest request) {
    AuthResponse response = loginUseCase.execute(request);
    return ResponseUtil.ok(response);
  }

  @GetMapping("/me")
  public ResponseEntity<CurrentUserResponse> me (Authentication authentication) {
    CurrentUserResponse response = getCurrentUserUseCase.execute(authentication.getName());
    return ResponseUtil.ok(response);
  }

  @PostMapping("/refresh")
  public ResponseEntity<RefreshTokenResponse> refresh (
      @Valid @RequestBody RefreshTokenRequest request) {
    RefreshTokenResponse response = refreshTokenUseCase.execute(request);
    return ResponseUtil.ok(response);
  }

  @PostMapping("/logout")
  public ResponseEntity<MessageResponse> logout (@Valid @RequestBody RefreshTokenRequest request) {
    logoutUseCase.execute(request.getRefreshToken());
    return ResponseUtil.ok(new MessageResponse("Đăng xuất thành công"));
  }

  @PostMapping("/forgot-password")
  public ResponseEntity<MessageResponse> forgotPassword (
      @Valid @RequestBody ForgotPasswordRequest request) {
    String token = forgotPasswordUseCase.execute(request);
    return ResponseUtil.ok(new MessageResponse("Reset token: " + token));
  }

  @PostMapping("/reset-password")
  public ResponseEntity<MessageResponse> resetPassword (
      @Valid @RequestBody ResetPasswordRequest request) {
    resetPasswordUseCase.execute(request);
    return ResponseUtil.ok(new MessageResponse("Đặt lại mật khẩu thành công"));
  }

  @PostMapping("/change-password")
  public ResponseEntity<MessageResponse> changePassword (Authentication authentication,
      @Valid @RequestBody ChangePasswordRequest request) {
    changePasswordUseCase.execute(authentication.getName(), request);
    return ResponseUtil.ok(new MessageResponse("Đổi mật khẩu thành công"));
  }
}