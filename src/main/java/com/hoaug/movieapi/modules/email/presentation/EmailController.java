package com.hoaug.movieapi.modules.email.presentation;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hoaug.movieapi.modules.email.application.EmailService;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/email")
@RequiredArgsConstructor
public class EmailController {

  private final EmailService emailService;

  
  @PostMapping("/send-forgot-password")
  public ResponseEntity<Map<String, Object>> sendForgotPasswordEmail (
      @RequestBody SendForgotPasswordEmailRequest request) {
    try {
      boolean sent = emailService.sendForgotPasswordEmail(request.getEmail(),
          request.getResetLink(), request.getFullName());

      Map<String, Object> response = new HashMap<>();
      response.put("success", sent);
      response.put("message", sent ? "Email gửi thành công" : "Lỗi gửi email");

      return ResponseEntity.ok(response);
    } catch (IOException e) {
      log.error("Error sending forgot password email", e);
      Map<String, Object> response = new HashMap<>();
      response.put("success", false);
      response.put("message", "Lỗi: " + e.getMessage());
      return ResponseEntity.badRequest().body(response);
    }
  }

  @PostMapping("/send-reset-password-success")
  public ResponseEntity<Map<String, Object>> sendResetPasswordSuccessEmail (
      @RequestParam String email, @RequestParam String fullName) {
    try {
      boolean sent = emailService.sendResetPasswordSuccessEmail(email, fullName);

      Map<String, Object> response = new HashMap<>();
      response.put("success", sent);
      response.put("message", sent ? "Email gửi thành công" : "Lỗi gửi email");

      return ResponseEntity.ok(response);
    } catch (IOException e) {
      log.error("Error sending reset password success email", e);
      Map<String, Object> response = new HashMap<>();
      response.put("success", false);
      response.put("message", "Lỗi: " + e.getMessage());
      return ResponseEntity.badRequest().body(response);
    }
  }

  @PostMapping("/send-signup-success")
  public ResponseEntity<Map<String, Object>> sendSignupSuccessEmail (
      @RequestBody SendEmailVerificationRequest request) {
    try {
      boolean sent = emailService.sendSignupSuccessEmail(request.getEmail(), request.getFullName(),
          request.getVerificationLink());

      Map<String, Object> response = new HashMap<>();
      response.put("success", sent);
      response.put("message", sent ? "Email gửi thành công" : "Lỗi gửi email");

      return ResponseEntity.ok(response);
    } catch (IOException e) {
      log.error("Error sending signup success email", e);
      Map<String, Object> response = new HashMap<>();
      response.put("success", false);
      response.put("message", "Lỗi: " + e.getMessage());
      return ResponseEntity.badRequest().body(response);
    }
  }

  @PostMapping("/send-email-verification")
  public ResponseEntity<Map<String, Object>> sendEmailVerificationEmail (
      @RequestBody SendEmailVerificationRequest request) {
    try {
      boolean sent = emailService.sendEmailVerificationEmail(request.getEmail(),
          request.getFullName(), request.getVerificationLink());

      Map<String, Object> response = new HashMap<>();
      response.put("success", sent);
      response.put("message", sent ? "Email gửi thành công" : "Lỗi gửi email");

      return ResponseEntity.ok(response);
    } catch (IOException e) {
      log.error("Error sending email verification", e);
      Map<String, Object> response = new HashMap<>();
      response.put("success", false);
      response.put("message", "Lỗi: " + e.getMessage());
      return ResponseEntity.badRequest().body(response);
    }
  }

  @PostMapping("/send-account-notification")
  public ResponseEntity<Map<String, Object>> sendAccountNotificationEmail (
      @RequestParam String email, @RequestParam String fullName,
      @RequestParam String notificationMessage) {
    try {
      boolean sent = emailService.sendAccountNotificationEmail(email, fullName,
          notificationMessage);

      Map<String, Object> response = new HashMap<>();
      response.put("success", sent);
      response.put("message", sent ? "Email gửi thành công" : "Lỗi gửi email");

      return ResponseEntity.ok(response);
    } catch (IOException e) {
      log.error("Error sending account notification email", e);
      Map<String, Object> response = new HashMap<>();
      response.put("success", false);
      response.put("message", "Lỗi: " + e.getMessage());
      return ResponseEntity.badRequest().body(response);
    }
  }

  @PostMapping("/send-new-movie-release")
  public ResponseEntity<Map<String, Object>> sendNewMovieReleaseEmail (
      @RequestBody SendNewMovieNotificationRequest request) {
    try {
      boolean sent = emailService.sendNewMovieReleaseEmail(request.getEmail(),
          request.getFullName(), request.getMovieTitle(), request.getMoviePosterUrl(),
          request.getMovieLink());

      Map<String, Object> response = new HashMap<>();
      response.put("success", sent);
      response.put("message", sent ? "Email gửi thành công" : "Lỗi gửi email");

      return ResponseEntity.ok(response);
    } catch (IOException e) {
      log.error("Error sending new movie release email", e);
      Map<String, Object> response = new HashMap<>();
      response.put("success", false);
      response.put("message", "Lỗi: " + e.getMessage());
      return ResponseEntity.badRequest().body(response);
    }
  }
}
