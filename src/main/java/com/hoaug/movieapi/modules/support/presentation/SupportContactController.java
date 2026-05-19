package com.hoaug.movieapi.modules.support.presentation;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hoaug.movieapi.modules.support.application.ContactMessageService;
import com.hoaug.movieapi.modules.support.application.TooManyContactRequestsException;
import com.hoaug.movieapi.modules.support.domain.ContactMessageRequest;
import com.hoaug.movieapi.modules.support.domain.ContactMessageResponse;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("${api.prefix:/api/v1}/support")
@RequiredArgsConstructor
public class SupportContactController {

  private final ContactMessageService contactMessageService;

  @PostMapping("/contact")
  public ResponseEntity<ContactMessageResponse> submit(
      @Valid @RequestBody ContactMessageRequest request,
      HttpServletRequest httpRequest) throws MessagingException, UnsupportedEncodingException {
    String clientIp = resolveClientIp(httpRequest);
    return ResponseEntity.ok(contactMessageService.submit(request, clientIp));
  }

  @ExceptionHandler(TooManyContactRequestsException.class)
  public ResponseEntity<Map<String, String>> handleRateLimit(TooManyContactRequestsException e) {
    return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
        .body(Map.of("message", e.getMessage()));
  }

  @ExceptionHandler(MessagingException.class)
  public ResponseEntity<Map<String, String>> handleMail(MessagingException e) {
    log.error("Failed to deliver contact email", e);
    return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
        .body(Map.of("message",
            "Hệ thống email tạm thời không khả dụng. Bạn vui lòng thử lại sau hoặc gửi trực tiếp tới hi@trkhoang.com."));
  }

  private String resolveClientIp(HttpServletRequest request) {
    String forwarded = request.getHeader("X-Forwarded-For");
    if (forwarded != null && !forwarded.isBlank()) {
      int comma = forwarded.indexOf(',');
      return (comma == -1 ? forwarded : forwarded.substring(0, comma)).trim();
    }
    String realIp = request.getHeader("X-Real-IP");
    if (realIp != null && !realIp.isBlank()) return realIp;
    return request.getRemoteAddr();
  }
}
