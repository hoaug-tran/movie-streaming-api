package com.hoaug.movieapi.modules.subscription.application.event;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.event.SubscriptionActivatedEvent;
import com.hoaug.movieapi.modules.notification.application.dto.request.CreateNotificationRequest;
import com.hoaug.movieapi.modules.notification.application.usecase.CreateNotificationUseCase;

@Component
public class SubscriptionEventListener {
  private final CreateNotificationUseCase createNotificationUseCase;

  public SubscriptionEventListener(CreateNotificationUseCase createNotificationUseCase) {
    this.createNotificationUseCase = createNotificationUseCase;
  }

  @EventListener
  public void onSubscriptionActivated (SubscriptionActivatedEvent event) {
    CreateNotificationRequest req = new CreateNotificationRequest();
    req.setTitle("Subscription Activated");
    req.setType("SUBSCRIPTION");
    createNotificationUseCase.execute(req);
  }
}
