package com.hoaug.movieapi.modules.notification.application.usecase;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.email.application.EmailService;
import com.hoaug.movieapi.modules.notification.application.dto.request.BroadcastNotificationRequest;
import com.hoaug.movieapi.modules.notification.domain.model.Notification;
import com.hoaug.movieapi.modules.notification.domain.model.NotificationType;
import com.hoaug.movieapi.modules.notification.domain.repository.NotificationRepository;

@Component
public class BroadcastNotificationUseCase {

  private static final Logger log = LoggerFactory.getLogger(BroadcastNotificationUseCase.class);

  private final NotificationRepository notificationRepository;
  private final EmailService emailService;

  public BroadcastNotificationUseCase(NotificationRepository notificationRepository,
      EmailService emailService) {
    this.notificationRepository = notificationRepository;
    this.emailService = emailService;
  }

  public int execute(BroadcastNotificationRequest request) {
    NotificationType type = NotificationType.valueOf(request.getType());
    LocalDateTime now = LocalDateTime.now();
    boolean sendInApp = request.isSendInApp();
    boolean sendEmail = request.isSendEmail();

    if (sendInApp) {
      List<Long> userIds = notificationRepository.findAllActiveUserIds();
      for (Long userId : userIds) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setTitle(request.getTitle());
        notification.setContent(request.getContent());
        notification.setType(type);
        notification.setIsRead(false);
        notification.setActionUrl(request.getActionUrl());
        notification.setCreatedAt(now);
        notificationRepository.save(notification);
      }
    }

    int emailCount = 0;
    if (sendEmail) {
      List<Object[]> users = notificationRepository.findAllActiveUsersWithEmail();
      for (Object[] row : users) {
        String email = (String) row[1];
        String fullName = row[2] != null ? (String) row[2] : email;
        try {
          emailService.sendAccountNotificationEmail(
              email,
              fullName,
              request.getTitle() + "\n\n" + request.getContent()
          );
          emailCount++;
        } catch (Exception e) {
          log.warn("Failed to send broadcast email to {}: {}", email, e.getMessage());
        }
      }
    }

    // Return total affected: in-app user count or email count, whichever is larger
    if (sendInApp && sendEmail) {
      return Math.max(notificationRepository.findAllActiveUserIds().size(), emailCount);
    } else if (sendEmail) {
      return emailCount;
    } else {
      return notificationRepository.findAllActiveUserIds().size();
    }
  }
}
