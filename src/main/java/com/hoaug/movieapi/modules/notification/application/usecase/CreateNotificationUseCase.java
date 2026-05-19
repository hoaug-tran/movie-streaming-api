package com.hoaug.movieapi.modules.notification.application.usecase;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.email.application.EmailService;
import com.hoaug.movieapi.modules.notification.application.dto.request.CreateNotificationRequest;
import com.hoaug.movieapi.modules.notification.application.dto.response.NotificationResponse;
import com.hoaug.movieapi.modules.notification.application.mapper.NotificationMapper;
import com.hoaug.movieapi.modules.notification.application.service.WebPushService;
import com.hoaug.movieapi.modules.notification.domain.model.Notification;
import com.hoaug.movieapi.modules.notification.domain.model.NotificationType;
import com.hoaug.movieapi.modules.notification.domain.repository.NotificationRepository;
import com.hoaug.movieapi.modules.user.domain.model.User;
import com.hoaug.movieapi.modules.user.domain.repository.UserRepository;

@Component
public class CreateNotificationUseCase {

  private static final Logger log = LoggerFactory.getLogger(CreateNotificationUseCase.class);

  private final NotificationRepository notificationRepository;
  private final NotificationMapper notificationMapper;
  private final EmailService emailService;
  private final UserRepository userRepository;
  private final WebPushService webPushService;

  public CreateNotificationUseCase(NotificationRepository notificationRepository,
      NotificationMapper notificationMapper,
      EmailService emailService,
      UserRepository userRepository,
      WebPushService webPushService) {
    this.notificationRepository = notificationRepository;
    this.notificationMapper = notificationMapper;
    this.emailService = emailService;
    this.userRepository = userRepository;
    this.webPushService = webPushService;
  }

  public NotificationResponse execute(CreateNotificationRequest request) {
    Notification savedNotification = null;

    // Save in-app notification
    if (request.isSendInApp()) {
      Notification notification = new Notification();
      notification.setUserId(request.getUserId());
      notification.setTitle(request.getTitle());
      notification.setContent(request.getContent());
      notification.setType(NotificationType.valueOf(request.getType()));
      notification.setIsRead(false);
      notification.setActionUrl(request.getActionUrl());
      notification.setReferenceId(request.getReferenceId());
      notification.setCreatedAt(LocalDateTime.now());
      savedNotification = notificationRepository.save(notification);
    }

    // Send email notification (fire-and-forget)
    if (request.isSendEmail()) {
      try {
        User user = userRepository.findById(request.getUserId()).orElse(null);
        if (user != null && user.getEmail() != null) {
          emailService.sendAccountNotificationEmail(
              user.getEmail(),
              user.getFullName() != null ? user.getFullName() : user.getUsername(),
              request.getTitle() + "\n\n" + request.getContent()
          );
        }
      } catch (Exception e) {
        log.warn("Failed to send email notification to userId={}: {}", request.getUserId(), e.getMessage());
      }
    }

    try {
      webPushService.sendToUser(request.getUserId(), request.getTitle(), request.getContent(), request.getActionUrl());
    } catch (Exception e) {
      log.warn("Failed to send web push to userId={}: {}", request.getUserId(), e.getMessage());
    }

    // If only email was sent (no in-app), create a minimal notification record for tracking
    if (savedNotification == null) {
      Notification notification = new Notification();
      notification.setUserId(request.getUserId());
      notification.setTitle(request.getTitle());
      notification.setContent(request.getContent());
      notification.setType(NotificationType.valueOf(request.getType()));
      notification.setIsRead(true); // mark read since it's email-only
      notification.setActionUrl(request.getActionUrl());
      notification.setReferenceId(request.getReferenceId());
      notification.setCreatedAt(LocalDateTime.now());
      savedNotification = notificationRepository.save(notification);
    }

    return notificationMapper.toResponse(savedNotification);
  }
}