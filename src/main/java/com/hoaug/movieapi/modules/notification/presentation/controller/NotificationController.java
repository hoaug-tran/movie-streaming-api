package com.hoaug.movieapi.modules.notification.presentation.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.auth.domain.repository.AuthUserRepository;
import com.hoaug.movieapi.modules.notification.application.dto.request.CreateNotificationRequest;
import com.hoaug.movieapi.modules.notification.application.dto.response.NotificationResponse;
import com.hoaug.movieapi.modules.notification.application.usecase.CreateNotificationUseCase;
import com.hoaug.movieapi.modules.notification.application.usecase.GetMyNotificationsUseCase;
import com.hoaug.movieapi.modules.notification.application.usecase.GetUnreadNotificationsCountUseCase;
import com.hoaug.movieapi.modules.notification.application.usecase.MarkAllNotificationsReadUseCase;
import com.hoaug.movieapi.modules.notification.application.usecase.MarkNotificationReadUseCase;
import com.hoaug.movieapi.modules.user.domain.model.User;

import jakarta.validation.Valid;

@RestController
@RequestMapping("${api.prefix:/api/v1}/notifications")
public class NotificationController {

  private final CreateNotificationUseCase createNotificationUseCase;
  private final GetMyNotificationsUseCase getMyNotificationsUseCase;
  private final MarkNotificationReadUseCase markNotificationReadUseCase;
  private final MarkAllNotificationsReadUseCase markAllNotificationsReadUseCase;
  private final GetUnreadNotificationsCountUseCase getUnreadNotificationsCountUseCase;
  private final AuthUserRepository authUserRepository;

  public NotificationController(CreateNotificationUseCase createNotificationUseCase,
      GetMyNotificationsUseCase getMyNotificationsUseCase,
      MarkNotificationReadUseCase markNotificationReadUseCase,
      MarkAllNotificationsReadUseCase markAllNotificationsReadUseCase,
      GetUnreadNotificationsCountUseCase getUnreadNotificationsCountUseCase,
      AuthUserRepository authUserRepository) {
    this.createNotificationUseCase = createNotificationUseCase;
    this.getMyNotificationsUseCase = getMyNotificationsUseCase;
    this.markNotificationReadUseCase = markNotificationReadUseCase;
    this.markAllNotificationsReadUseCase = markAllNotificationsReadUseCase;
    this.getUnreadNotificationsCountUseCase = getUnreadNotificationsCountUseCase;
    this.authUserRepository = authUserRepository;
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping
  public NotificationResponse create (@Valid @RequestBody CreateNotificationRequest request) {
    return createNotificationUseCase.execute(request);
  }

  @GetMapping("/me")
  public List<NotificationResponse> getMyNotifications (Authentication authentication) {
    return getMyNotificationsUseCase.execute(getCurrentUserId(authentication));
  }

  @PatchMapping("/{notificationId}/read")
  public NotificationResponse markRead (Authentication authentication,
      @PathVariable Long notificationId) {
    return markNotificationReadUseCase.execute(getCurrentUserId(authentication), notificationId);
  }

  @PatchMapping("/me/read-all")
  public void markAllRead (Authentication authentication) {
    markAllNotificationsReadUseCase.execute(getCurrentUserId(authentication));
  }

  @GetMapping("/me/unread-count")
  public Long getUnreadCount (Authentication authentication) {
    return getUnreadNotificationsCountUseCase.execute(getCurrentUserId(authentication));
  }

  private Long getCurrentUserId (Authentication authentication) {
    User user = authUserRepository.findByUsername(authentication.getName())
        .orElseThrow( () -> new AppException(ErrorCode.USER_NOT_FOUND));
    return user.getId();
  }
}