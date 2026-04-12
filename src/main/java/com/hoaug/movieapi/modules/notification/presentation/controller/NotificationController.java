package com.hoaug.movieapi.modules.notification.presentation.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.common.response.ResponseUtil;
import com.hoaug.movieapi.modules.auth.domain.repository.AuthUserRepository;
import com.hoaug.movieapi.modules.notification.application.dto.request.CreateNotificationRequest;
import com.hoaug.movieapi.modules.notification.application.dto.response.NotificationResponse;
import com.hoaug.movieapi.modules.notification.application.usecase.CreateNotificationUseCase;
import com.hoaug.movieapi.modules.notification.application.usecase.DeleteNotificationUseCase;
import com.hoaug.movieapi.modules.notification.application.usecase.GetMyNotificationsUseCase;
import com.hoaug.movieapi.modules.notification.application.usecase.GetUnreadNotificationsCountUseCase;
import com.hoaug.movieapi.modules.notification.application.usecase.MarkAllNotificationsAsReadUseCase;
import com.hoaug.movieapi.modules.notification.application.usecase.MarkNotificationAsReadUseCase;
import com.hoaug.movieapi.modules.user.domain.model.User;

import jakarta.validation.Valid;

@RestController
@RequestMapping("${api.prefix:/api/v1}/notifications")
public class NotificationController {

  private final CreateNotificationUseCase createNotificationUseCase;
  private final GetMyNotificationsUseCase getMyNotificationsUseCase;
  private final MarkNotificationAsReadUseCase markNotificationAsReadUseCase;
  private final MarkAllNotificationsAsReadUseCase markAllNotificationsAsReadUseCase;
  private final DeleteNotificationUseCase deleteNotificationUseCase;
  private final GetUnreadNotificationsCountUseCase getUnreadNotificationsCountUseCase;
  private final AuthUserRepository authUserRepository;

  public NotificationController(CreateNotificationUseCase createNotificationUseCase,
      GetMyNotificationsUseCase getMyNotificationsUseCase,
      MarkNotificationAsReadUseCase markNotificationAsReadUseCase,
      MarkAllNotificationsAsReadUseCase markAllNotificationsAsReadUseCase,
      DeleteNotificationUseCase deleteNotificationUseCase,
      GetUnreadNotificationsCountUseCase getUnreadNotificationsCountUseCase,
      AuthUserRepository authUserRepository) {
    this.createNotificationUseCase = createNotificationUseCase;
    this.getMyNotificationsUseCase = getMyNotificationsUseCase;
    this.markNotificationAsReadUseCase = markNotificationAsReadUseCase;
    this.markAllNotificationsAsReadUseCase = markAllNotificationsAsReadUseCase;
    this.deleteNotificationUseCase = deleteNotificationUseCase;
    this.getUnreadNotificationsCountUseCase = getUnreadNotificationsCountUseCase;
    this.authUserRepository = authUserRepository;
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping
  public ResponseEntity<NotificationResponse> create (
      @Valid @RequestBody CreateNotificationRequest request) {
    return ResponseUtil.created(createNotificationUseCase.execute(request));
  }

  @GetMapping("/me")
  public ResponseEntity<List<NotificationResponse>> getMyNotifications (
      Authentication authentication) {
    return ResponseUtil.ok(getMyNotificationsUseCase.execute(getCurrentUserId(authentication)));
  }

  @PatchMapping("/{notificationId}/read")
  public ResponseEntity<Void> markRead (Authentication authentication,
      @PathVariable Long notificationId) {
    markNotificationAsReadUseCase.execute(getCurrentUserId(authentication), notificationId);
    return ResponseUtil.noContent();
  }

  @PatchMapping("/me/read-all")
  public ResponseEntity<Void> markAllRead (Authentication authentication) {
    markAllNotificationsAsReadUseCase.execute(getCurrentUserId(authentication));
    return ResponseUtil.noContent();
  }

  @DeleteMapping("/{notificationId}")
  public ResponseEntity<Void> delete (Authentication authentication,
      @PathVariable Long notificationId) {
    deleteNotificationUseCase.execute(getCurrentUserId(authentication), notificationId);
    return ResponseUtil.noContent();
  }

  @GetMapping("/me/unread-count")
  public ResponseEntity<Long> getUnreadCount (Authentication authentication) {
    return ResponseUtil
        .ok(getUnreadNotificationsCountUseCase.execute(getCurrentUserId(authentication)));
  }

  private Long getCurrentUserId (Authentication authentication) {
    User user = authUserRepository.findByUsername(authentication.getName())
        .orElseThrow( () -> new AppException(ErrorCode.USER_NOT_FOUND));
    return user.getId();
  }
}