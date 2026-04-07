package com.hoaug.movieapi.common.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class EventPublisherImpl implements EventPublisher {
  private final ApplicationEventPublisher applicationEventPublisher;

  public EventPublisherImpl(ApplicationEventPublisher applicationEventPublisher) {
    this.applicationEventPublisher = applicationEventPublisher;
  }

  @Override
  public void publish (DomainEvent event) {
    applicationEventPublisher.publishEvent(event);
  }
}
