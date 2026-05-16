package com.hoaug.movieapi.modules.notification.application.scheduler;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.movie.domain.model.Movie;
import com.hoaug.movieapi.modules.movie.domain.repository.MovieRepository;
import com.hoaug.movieapi.modules.notification.domain.model.Notification;
import com.hoaug.movieapi.modules.notification.domain.model.NotificationType;
import com.hoaug.movieapi.modules.notification.domain.repository.NotificationRepository;
import com.hoaug.movieapi.modules.subscription.domain.model.SubscriptionStatus;
import com.hoaug.movieapi.modules.subscription.domain.model.UserSubscription;
import com.hoaug.movieapi.modules.subscription.domain.repository.SubscriptionPlanRepository;
import com.hoaug.movieapi.modules.subscription.domain.repository.UserSubscriptionRepository;

@Component
public class NotificationScheduler {

  private final NotificationRepository notificationRepository;
  private final UserSubscriptionRepository userSubscriptionRepository;
  private final SubscriptionPlanRepository subscriptionPlanRepository;
  private final MovieRepository movieRepository;

  public NotificationScheduler(NotificationRepository notificationRepository,
      UserSubscriptionRepository userSubscriptionRepository,
      SubscriptionPlanRepository subscriptionPlanRepository, MovieRepository movieRepository) {
    this.notificationRepository = notificationRepository;
    this.userSubscriptionRepository = userSubscriptionRepository;
    this.subscriptionPlanRepository = subscriptionPlanRepository;
    this.movieRepository = movieRepository;
  }

  @Scheduled(cron = "0 0 9 * * *")
  public void sendPremiumExpiringNotifications () {
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime in2Days = now.plusDays(2);
    LocalDateTime in1Day = now.plusDays(1);

    List<UserSubscription> expiringSoon = userSubscriptionRepository
        .findByStatusAndEndAtBetween(SubscriptionStatus.ACTIVE, in1Day, in2Days);

    for (UserSubscription sub : expiringSoon) {
      boolean alreadyNotified = notificationRepository.existsByUserIdAndTypeAndCreatedAtAfter(
          sub.getUserId(), NotificationType.PREMIUM_EXPIRING.name(), now.minusHours(23));
      if (alreadyNotified)
        continue;

      String planName = subscriptionPlanRepository.findById(sub.getPlanId()).map(p -> p.getName())
          .orElse("Premium");

      Notification notification = new Notification();
      notification.setUserId(sub.getUserId());
      notification.setTitle("Gói Premium sắp hết hạn ⚠️");
      notification.setContent("Gói " + planName
          + " của bạn sẽ hết hạn trong vòng 2 ngày. Gia hạn ngay để không bị gián đoạn dịch vụ!");
      notification.setType(NotificationType.PREMIUM_EXPIRING);
      notification.setIsRead(false);
      notification.setActionUrl("/subscription/plans");
      notification.setReferenceId(sub.getId());
      notification.setCreatedAt(LocalDateTime.now());
      notificationRepository.save(notification);
    }
  }

  @Scheduled(cron = "0 5 0 * * *")
  public void sendSubscriptionExpiredNotifications () {
    LocalDateTime now = LocalDateTime.now();

    List<UserSubscription> expired = userSubscriptionRepository
        .findByStatusAndEndAtBefore(SubscriptionStatus.ACTIVE, now);

    for (UserSubscription sub : expired) {
      boolean alreadyNotified = notificationRepository.existsByUserIdAndTypeAndCreatedAtAfter(
          sub.getUserId(), NotificationType.SUBSCRIPTION_EXPIRED.name(), now.minusHours(20));
      if (alreadyNotified)
        continue;

      String planName = subscriptionPlanRepository.findById(sub.getPlanId()).map(p -> p.getName())
          .orElse("Premium");

      Notification notification = new Notification();
      notification.setUserId(sub.getUserId());
      notification.setTitle("Gói Premium đã hết hạn 😢");
      notification.setContent("Gói " + planName
          + " của bạn đã hết hạn. Đăng ký lại để tiếp tục xem phim không giới hạn!");
      notification.setType(NotificationType.SUBSCRIPTION_EXPIRED);
      notification.setIsRead(false);
      notification.setActionUrl("/subscription/plans");
      notification.setReferenceId(sub.getId());
      notification.setCreatedAt(LocalDateTime.now());
      notificationRepository.save(notification);
    }
  }

  @Scheduled(cron = "0 0 10 * * MON")
  public void sendHotMoviesWeeklyNotifications () {
    List<Movie> hotMovies = movieRepository.findTopTrending(3);
    if (hotMovies.isEmpty())
      return;

    List<Long> userIds = notificationRepository.findAllActiveUserIds();

    StringBuilder content = new StringBuilder("Phim hot tuần này: ");
    for (int i = 0; i < hotMovies.size(); i++) {
      if (i > 0)
        content.append(", ");
      content.append(hotMovies.get(i).getTitle());
    }
    content.append(". Xem ngay!");

    LocalDateTime now = LocalDateTime.now();
    for (Long userId : userIds) {
      boolean alreadyNotified = notificationRepository.existsByUserIdAndTypeAndCreatedAtAfter(
          userId, NotificationType.HOT_MOVIES.name(), now.minusDays(6));
      if (alreadyNotified)
        continue;

      Notification notification = new Notification();
      notification.setUserId(userId);
      notification.setTitle("🔥 Phim hot tuần này không thể bỏ lỡ!");
      notification.setContent(content.toString());
      notification.setType(NotificationType.HOT_MOVIES);
      notification.setIsRead(false);
      notification.setActionUrl("/discovery");
      notification.setCreatedAt(now);
      notificationRepository.save(notification);
    }
  }
}
