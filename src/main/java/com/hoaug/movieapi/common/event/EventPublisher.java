package com.hoaug.movieapi.common.event;

public interface EventPublisher {
  void publish (DomainEvent event);
}
