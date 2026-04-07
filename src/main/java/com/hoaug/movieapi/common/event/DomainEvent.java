package com.hoaug.movieapi.common.event;

import java.time.LocalDateTime;

public abstract class DomainEvent {
  private final LocalDateTime occurredAt;

  public DomainEvent() {
    this.occurredAt = LocalDateTime.now();
  }

  public LocalDateTime getOccurredAt () {
    return occurredAt;
  }
}
