package com.hoaug.movieapi.modules.auth.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hoaug.movieapi.common.response.ResponseUtil;
import com.hoaug.movieapi.common.util.CookieUtil;
import com.hoaug.movieapi.modules.auth.application.dto.request.ChangePasswordRequest;
import com.hoaug.movieapi.modules.auth.application.dto.request.ForgotPasswordRequest;
import com.hoaug.movieapi.modules.auth.application.dto.request.LoginRequest;
import com.hoaug.movieapi.modules.auth.application.dto.request.RefreshTokenRequest;
import com.hoaug.movieapi.modules.auth.application.dto.request.RegisterRequest;
import com.hoaug.movieapi.modules.auth.application.dto.request.StartChangePasswordRequest;
import com.hoaug.movieapi.modules.auth.application.dto.request.VerifyOtpRequest;
import com.hoaug.movieapi.modules.auth.application.dto.request.VerifyPasswordResetOtpRequest;
import com.hoaug.movieapi.modules.auth.application.dto.response.AuthResponse;
import com.hoaug.movieapi.modules.auth.application.dto.response.CurrentUserResponse;
import com.hoaug.movieapi.modules.auth.application.dto.response.MessageResponse;
import com.hoaug.movieapi.modules.auth.application.dto.response.OtpChallengeResponse;
import com.hoaug.movieapi.modules.auth.application.dto.response.RefreshTokenResponse;
import com.hoaug.movieapi.modules.auth.application.usecase.ChangePasswordUseCase;
import com.hoaug.movieapi.modules.auth.application.usecase.ForgotPasswordUseCase;
import com.hoaug.movieapi.modules.auth.application.usecase.GetCurrentUserUseCase;
import com.hoaug.movieapi.modules.auth.application.usecase.LoginUseCase;
import com.hoaug.movieapi.modules.auth.application.usecase.LogoutUseCase;
import com.hoaug.movieapi.modules.auth.application.usecase.RefreshTokenUseCase;
import com.hoaug.movieapi.modules.auth.application.usecase.RegisterUseCase;
import com.hoaug.movieapi.modules.auth.application.usecase.ResetPasswordUseCase;
import com.hoaug.movieapi.modules.auth.application.dto.response.LoginResult;
import com.hoaug.movieapi.modules.auth.application.usecase.StartChangePasswordUseCase;
import com.hoaug.movieapi.modules.auth.application.usecase.VerifyLoginOtpUseCase;
import com.hoaug.movieapi.modules.auth.application.usecase.VerifyRegisterOtpUseCase;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("${api.prefix:/api/v1}/auth")
public class AuthController {

  private final RegisterUseCase registerUseCase;
  private final LoginUseCase loginUseCase;
  private final VerifyLoginOtpUseCase verifyLoginOtpUseCase;
  private final VerifyRegisterOtpUseCase verifyRegisterOtpUseCase;
  private final GetCurrentUserUseCase getCurrentUserUseCase;
  private final RefreshTokenUseCase refreshTokenUseCase;
  private final LogoutUseCase logoutUseCase;
  private final ForgotPasswordUseCase forgotPasswordUseCase;
  private final ResetPasswordUseCase resetPasswordUseCase;
  private final ChangePasswordUseCase changePasswordUseCase;
  private final StartChangePasswordUseCase startChangePasswordUseCase;
  private final CookieUtil cookieUtil;

  public AuthController(RegisterUseCase registerUseCase, LoginUseCase loginUseCase,
      VerifyLoginOtpUseCase verifyLoginOtpUseCase,
      VerifyRegisterOtpUseCase verifyRegisterOtpUseCase,
      GetCurrentUserUseCase getCurrentUserUseCase, RefreshTokenUseCase refreshTokenUseCase,
      LogoutUseCase logoutUseCase, ForgotPasswordUseCase forgotPasswordUseCase,
      ResetPasswordUseCase resetPasswordUseCase, ChangePasswordUseCase changePasswordUseCase,
      StartChangePasswordUseCase startChangePasswordUseCase, CookieUtil cookieUtil) {
    this.registerUseCase = registerUseCase;
    this.loginUseCase = loginUseCase;
    this.verifyLoginOtpUseCase = verifyLoginOtpUseCase;
    this.verifyRegisterOtpUseCase = verifyRegisterOtpUseCase;
    this.getCurrentUserUseCase = getCurrentUserUseCase;
    this.refreshTokenUseCase = refreshTokenUseCase;
    this.logoutUseCase = logoutUseCase;
    this.forgotPasswordUseCase = forgotPasswordUseCase;
    this.resetPasswordUseCase = resetPasswordUseCase;
    this.changePasswordUseCase = changePasswordUseCase;
    this.startChangePasswordUseCase = startChangePasswordUseCase;
    this.cookieUtil = cookieUtil;
  }

  @PostMapping("/register")
  public ResponseEntity<OtpChallengeResponse> register (@Valid @RequestBody RegisterRequest request) {
    return ResponseUtil.created(registerUseCase.execute(request));
  }

  @PostMapping("/register/verify-otp")
  public ResponseEntity<AuthResponse> verifyRegisterOtp (@Valid @RequestBody VerifyOtpRequest request,
      HttpServletResponse response) {
    AuthResponse authResponse = verifyRegisterOtpUseCase.execute(request);
    cookieUtil.setAuthCookies(response, authResponse);
    return ResponseUtil.ok(authResponse);
  }

  @PostMapping("/login")
  public ResponseEntity<?> login (@Valid @RequestBody LoginRequest request,
      HttpServletRequest httpRequest, HttpServletResponse response) {
    String trustedDeviceToken = CookieUtil.getCookieValue(httpRequest, "trusted_device");
    LoginResult result = loginUseCase.execute(request, trustedDeviceToken);
    if (result.isDirectAuth()) {
      cookieUtil.setAuthCookies(response, result.getAuthResponse());
      return ResponseUtil.ok(result.getAuthResponse());
    }
    return ResponseUtil.ok(result.getOtpChallenge());
  }

  @PostMapping("/login/verify-otp")
  public ResponseEntity<AuthResponse> verifyLoginOtp (@Valid @RequestBody VerifyOtpRequest request,
      HttpServletRequest httpRequest, HttpServletResponse response) {
    VerifyLoginOtpUseCase.Result result = verifyLoginOtpUseCase.execute(request);
    cookieUtil.setAuthCookies(response, result.authResponse());
    if (result.trustedDeviceCookie() != null) {
      CookieUtil.addCookie(response, result.trustedDeviceCookie());
    }
    return ResponseUtil.ok(result.authResponse());
  }

  @GetMapping("/me")
  public ResponseEntity<CurrentUserResponse> me (Authentication authentication) {
    if (authentication == null) {
      return ResponseEntity.status(401).build();
    }
    return ResponseUtil.ok(getCurrentUserUseCase.execute(authentication.getName()));
  }

  @PostMapping("/refresh")
  public ResponseEntity<RefreshTokenResponse> refresh (
      @Valid @RequestBody(required = false) RefreshTokenRequest request, HttpServletRequest httpRequest,
      HttpServletResponse response) {
    String refreshToken = request != null ? request.getRefreshToken() : null;
    if (refreshToken == null || refreshToken.isBlank()) {
      refreshToken = CookieUtil.getCookieValue(httpRequest, "refreshToken");
    }

    RefreshTokenResponse refreshTokenResponse = refreshTokenUseCase.execute(refreshToken);

    AuthResponse authTokens = new AuthResponse();
    authTokens.setAccessToken(refreshTokenResponse.getAccessToken());
    authTokens.setRefreshToken(refreshTokenResponse.getRefreshToken());
    cookieUtil.setAuthCookies(response, authTokens);

    return ResponseUtil.ok(refreshTokenResponse);
  }

  @PostMapping("/logout")
  public ResponseEntity<MessageResponse> logout (
      @RequestBody(required = false) RefreshTokenRequest request,
      HttpServletRequest httpRequest, HttpServletResponse response) {

    String tokenToRevoke = request != null ? request.getRefreshToken() : null;
    if (tokenToRevoke == null) {
      tokenToRevoke = CookieUtil.getCookieValue(httpRequest, "refreshToken");
    }

    if (tokenToRevoke != null) {
      logoutUseCase.execute(tokenToRevoke);
    }

    cookieUtil.clearAuthCookies(response);
    return ResponseUtil.ok(new MessageResponse("Đăng xuất thành công"));
  }

  @PostMapping("/forgot-password")
  public ResponseEntity<OtpChallengeResponse> forgotPassword (
      @Valid @RequestBody ForgotPasswordRequest request) {
    return ResponseUtil.ok(forgotPasswordUseCase.execute(request));
  }

  @PostMapping("/reset-password")
  public ResponseEntity<MessageResponse> resetPassword (
      @Valid @RequestBody VerifyPasswordResetOtpRequest request) {
    resetPasswordUseCase.execute(request);
    return ResponseUtil.ok(new MessageResponse("Đặt lại mật khẩu thành công"));
  }

  @PostMapping("/change-password/start")
  public ResponseEntity<OtpChallengeResponse> startChangePassword (Authentication authentication,
      @Valid @RequestBody StartChangePasswordRequest request) {
    return ResponseUtil.ok(startChangePasswordUseCase.execute(authentication.getName(), request));
  }

  @PostMapping("/change-password")
  public ResponseEntity<MessageResponse> changePassword (Authentication authentication,
      @Valid @RequestBody ChangePasswordRequest request) {
    changePasswordUseCase.execute(authentication.getName(), request);
    return ResponseUtil.ok(new MessageResponse("Đổi mật khẩu thành công"));
  }
}