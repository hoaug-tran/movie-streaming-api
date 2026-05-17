package com.hoaug.movieapi.modules.subscription.application.scheduler;

import java.time.LocalDateTime;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.subscription.domain.model.SubscriptionStatus;
import com.hoaug.movieapi.modules.subscription.domain.repository.UserSubscriptionRepository;
import com.hoaug.movieapi.modules.user.domain.model.User;
import com.hoaug.movieapi.modules.user.domain.repository.UserRepository;

@Component
public class SubscriptionScheduler {
  private final UserSubscriptionRepository userSubscriptionRepository;
  private final UserRepository userRepository;

  public SubscriptionScheduler(UserSubscriptionRepository userSubscriptionRepository,
      UserRepository userRepository) {
    this.userSubscriptionRepository = userSubscriptionRepository;
    this.userRepository = userRepository;
  }

  @Scheduled(fixedRate = 3600000)
  public void expireSubscriptions () {
    var expiredSubscriptions = userSubscriptionRepository
        .findByStatusAndEndAtBefore(SubscriptionStatus.ACTIVE, LocalDateTime.now());

    expiredSubscriptions.forEach(sub -> {
      sub.setStatus(SubscriptionStatus.EXPIRED);
      sub.setUpdatedAt(LocalDateTime.now());
      userSubscriptionRepository.save(sub);
    });
  }

  @Scheduled(fixedRate = 86400000)
  public void deactivatePremiumUsers () {
    var expiredSubscriptions = userSubscriptionRepository
        .findByStatusAndEndAtBefore(SubscriptionStatus.ACTIVE, LocalDateTime.now());

    expiredSubscriptions.forEach(sub -> {
      var user = userRepository.findById(sub.getUserId());
      if (user.isPresent()) {
        User u = user.get();
        u.setUpdatedAt(LocalDateTime.now());
        userRepository.save(u);
      }
    });
  }
}
