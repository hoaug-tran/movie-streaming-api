package com.hoaug.movieapi.modules.notification.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.auth.domain.repository.AuthUserRepository;
import com.hoaug.movieapi.modules.notification.infrastructure.persistence.entity.PushSubscriptionEntity;
import com.hoaug.movieapi.modules.notification.infrastructure.persistence.repository.JpaPushSubscriptionRepository;
import com.hoaug.movieapi.modules.user.domain.model.User;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@RestController
@RequestMapping("${api.prefix:/api/v1}/push")
public class PushSubscriptionController {

  private final JpaPushSubscriptionRepository pushSubscriptionRepository;
  private final AuthUserRepository authUserRepository;

  public PushSubscriptionController (JpaPushSubscriptionRepository pushSubscriptionRepository,
      AuthUserRepository authUserRepository) {
    this.pushSubscriptionRepository = pushSubscriptionRepository;
    this.authUserRepository = authUserRepository;
  }

  public record SubscribeRequest(
      @NotBlank String endpoint,
      String p256dh,
      String auth
  ) {}

  public record UnsubscribeRequest(@NotBlank String endpoint) {}

  @PostMapping("/subscribe")
  @Transactional
  public ResponseEntity<Void> subscribe (
      @Valid @RequestBody SubscribeRequest request,
      Authentication authentication) {

    User user = getUser(authentication);

    pushSubscriptionRepository.findByUserIdAndEndpoint(user.getId(), request.endpoint())
        .ifPresentOrElse(
            existing -> {
              existing.setP256dh(request.p256dh());
              existing.setAuth(request.auth());
              pushSubscriptionRepository.save(existing);
            },
            () -> {
              PushSubscriptionEntity entity = new PushSubscriptionEntity();
              entity.setUserId(user.getId());
              entity.setEndpoint(request.endpoint());
              entity.setP256dh(request.p256dh());
              entity.setAuth(request.auth());
              pushSubscriptionRepository.save(entity);
            }
        );

    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/unsubscribe")
  @Transactional
  public ResponseEntity<Void> unsubscribe (
      @Valid @RequestBody UnsubscribeRequest request,
      Authentication authentication) {

    User user = getUser(authentication);
    pushSubscriptionRepository.deleteByUserIdAndEndpoint(user.getId(), request.endpoint());
    return ResponseEntity.noContent().build();
  }

  private User getUser (Authentication authentication) {
    if (authentication == null || !authentication.isAuthenticated()
        || "anonymousUser".equals(authentication.getPrincipal())) {
      throw new AppException(ErrorCode.FORBIDDEN);
    }
    return authUserRepository.findByUsername(authentication.getName())
        .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
  }
}
