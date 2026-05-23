package com.hoaug.movieapi.modules.notification.application.usecase;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.email.application.EmailService;
import com.hoaug.movieapi.modules.notification.application.dto.request.BroadcastNotificationRequest;
import com.hoaug.movieapi.modules.notification.application.service.WebPushService;
import com.hoaug.movieapi.modules.notification.domain.model.Notification;
import com.hoaug.movieapi.modules.notification.domain.model.NotificationType;
import com.hoaug.movieapi.modules.notification.domain.repository.NotificationRepository;
import com.hoaug.movieapi.modules.user.domain.repository.UserRepository;
import com.hoaug.movieapi.modules.activitylog.application.service.ActivityLogService;
import com.hoaug.movieapi.modules.activitylog.domain.model.ActivityScope;
import com.hoaug.movieapi.modules.activitylog.domain.model.ActivitySeverity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Component
public class BroadcastNotificationUseCase {

  private static final Logger log = LoggerFactory.getLogger(BroadcastNotificationUseCase.class);

  private final NotificationRepository notificationRepository;
  private final EmailService emailService;
  private final WebPushService webPushService;
  private final UserRepository userRepository;
  private final ActivityLogService activityLogService;

  public BroadcastNotificationUseCase(NotificationRepository notificationRepository,
      EmailService emailService,
      WebPushService webPushService,
      UserRepository userRepository,
      ActivityLogService activityLogService) {
    this.notificationRepository = notificationRepository;
    this.emailService = emailService;
    this.webPushService = webPushService;
    this.userRepository = userRepository;
    this.activityLogService = activityLogService;
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

    try {
      webPushService.sendToAll(request.getTitle(), request.getContent(), request.getActionUrl());
    } catch (Exception e) {
      log.warn("Failed to send broadcast web push: {}", e.getMessage());
    }

    int totalCount = 0;
    if (sendInApp && sendEmail) {
      totalCount = Math.max(notificationRepository.findAllActiveUserIds().size(), emailCount);
    } else if (sendEmail) {
      totalCount = emailCount;
    } else {
      totalCount = notificationRepository.findAllActiveUserIds().size();
    }

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    Long actorId = 0L;
    String actorName = "Hệ thống";
    if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
      var opt = userRepository.findByUsername(auth.getName());
      if (opt.isPresent()) {
        actorId = opt.get().getId();
        actorName = opt.get().getFullName();
      }
    }

    activityLogService.record(
      ActivityScope.ADMIN,
      actorId,
      actorName,
      "Phát thông báo",
      "NOTIFICATION",
      null,
      request.getTitle(),
      actorName + " đã phát thông báo hệ thống: " + request.getTitle(),
      ActivitySeverity.INFO
    );

    return totalCount;
  }
}
