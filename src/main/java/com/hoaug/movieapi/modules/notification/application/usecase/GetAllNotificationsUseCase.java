package com.hoaug.movieapi.modules.notification.application.usecase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.notification.application.dto.response.NotificationResponse;
import com.hoaug.movieapi.modules.notification.application.mapper.NotificationMapper;
import com.hoaug.movieapi.modules.notification.domain.model.Notification;
import com.hoaug.movieapi.modules.notification.domain.repository.NotificationRepository;
import com.hoaug.movieapi.modules.user.domain.model.User;
import com.hoaug.movieapi.modules.user.domain.repository.UserRepository;

@Component
public class GetAllNotificationsUseCase {

  private final NotificationRepository notificationRepository;
  private final NotificationMapper notificationMapper;
  private final UserRepository userRepository;

  public GetAllNotificationsUseCase(NotificationRepository notificationRepository,
      NotificationMapper notificationMapper,
      UserRepository userRepository) {
    this.notificationRepository = notificationRepository;
    this.notificationMapper = notificationMapper;
    this.userRepository = userRepository;
  }

  public List<NotificationResponse> execute() {
    List<Notification> notifications = notificationRepository.findAll();
    List<Long> userIds = notifications.stream()
        .map(Notification::getUserId)
        .filter(Objects::nonNull)
        .distinct()
        .toList();
    Map<Long, User> userMap = new HashMap<>();
    for (Long userId : userIds) {
      Optional<User> userOpt = userRepository.findById(userId);
      userOpt.ifPresent(user -> userMap.put(user.getId(), user));
    }
    return notifications.stream()
        .map(notification -> {
          NotificationResponse response = notificationMapper.toResponse(notification);
          if (notification.getUserId() != null) {
            User user = userMap.get(notification.getUserId());
            if (user != null) {
              response.setUserFullName(user.getFullName());
              response.setUserUsername(user.getUsername());
            }
          }
          return response;
        })
        .toList();
  }
}
