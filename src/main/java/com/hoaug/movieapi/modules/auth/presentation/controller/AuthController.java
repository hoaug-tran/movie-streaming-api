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
  public ResponseEntity<AuthResponse> register (@Valid @RequestBody RegisterRequest request,
      jakarta.servlet.http.HttpServletResponse response) {
    AuthResponse authResponse = registerUseCase.execute(request);
    setAuthCookies(response, authResponse);
    return ResponseUtil.created(authResponse);
  }

  @PostMapping("/login")
  public ResponseEntity<AuthResponse> login (@Valid @RequestBody LoginRequest request,
      jakarta.servlet.http.HttpServletResponse response) {
    AuthResponse authResponse = loginUseCase.execute(request);
    setAuthCookies(response, authResponse);
    return ResponseUtil.ok(authResponse);
  }

  @GetMapping("/me")
  public ResponseEntity<CurrentUserResponse> me (Authentication authentication) {
    if (authentication == null) {
      return ResponseEntity.status(401).build();
    }
    CurrentUserResponse response = getCurrentUserUseCase.execute(authentication.getName());
    return ResponseUtil.ok(response);
  }

  @PostMapping("/refresh")
  public ResponseEntity<RefreshTokenResponse> refresh (
      @Valid @RequestBody RefreshTokenRequest request,
      jakarta.servlet.http.HttpServletResponse response) {
    RefreshTokenResponse refreshTokenResponse = refreshTokenUseCase.execute(request);

    // Cập nhật lại accessToken cookie
    jakarta.servlet.http.Cookie cookie = new jakarta.servlet.http.Cookie("accessToken",
        refreshTokenResponse.getAccessToken());
    cookie.setHttpOnly(true);
    cookie.setSecure(false); // TODO Đặt thành true nếu dùng HTTPS
    cookie.setPath("/");
    cookie.setMaxAge(3600); // 1 giờ
    response.addCookie(cookie);

    return ResponseUtil.ok(refreshTokenResponse);
  }

  @PostMapping("/logout")
  public ResponseEntity<MessageResponse> logout (
      @RequestBody(required = false) RefreshTokenRequest request,
      jakarta.servlet.http.HttpServletRequest httpRequest,
      jakarta.servlet.http.HttpServletResponse response) {

    String tokenToRevoke = null;
    if (request != null && request.getRefreshToken() != null) {
      tokenToRevoke = request.getRefreshToken();
    } else if (httpRequest.getCookies() != null) {
      for (jakarta.servlet.http.Cookie cookie : httpRequest.getCookies()) {
        if ("refreshToken".equals(cookie.getName())) {
          tokenToRevoke = cookie.getValue();
          break;
        }
      }
    }

    if (tokenToRevoke != null) {
      logoutUseCase.execute(tokenToRevoke);
    }

    // Xóa cookies
    clearAuthCookies(response);

    return ResponseUtil.ok(new MessageResponse("Đăng xuất thành công"));
  }

  private void setAuthCookies (jakarta.servlet.http.HttpServletResponse response,
      AuthResponse authResponse) {
    // Access Token Cookie
    jakarta.servlet.http.Cookie accessCookie = new jakarta.servlet.http.Cookie("accessToken",
        authResponse.getAccessToken());
    accessCookie.setHttpOnly(true);
    accessCookie.setSecure(false);
    accessCookie.setPath("/");
    accessCookie.setMaxAge(3600); // 1 giờ
    response.addCookie(accessCookie);

    // Refresh Token Cookie
    jakarta.servlet.http.Cookie refreshCookie = new jakarta.servlet.http.Cookie("refreshToken",
        authResponse.getRefreshToken());
    refreshCookie.setHttpOnly(true);
    refreshCookie.setSecure(false);
    refreshCookie.setPath("/");
    refreshCookie.setMaxAge(30 * 24 * 60 * 60); // 30 ngày
    response.addCookie(refreshCookie);
  }

  private void clearAuthCookies (jakarta.servlet.http.HttpServletResponse response) {
    jakarta.servlet.http.Cookie accessCookie = new jakarta.servlet.http.Cookie("accessToken", null);
    accessCookie.setPath("/");
    accessCookie.setMaxAge(0);
    response.addCookie(accessCookie);

    jakarta.servlet.http.Cookie refreshCookie = new jakarta.servlet.http.Cookie("refreshToken",
        null);
    refreshCookie.setPath("/");
    refreshCookie.setMaxAge(0);
    response.addCookie(refreshCookie);
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