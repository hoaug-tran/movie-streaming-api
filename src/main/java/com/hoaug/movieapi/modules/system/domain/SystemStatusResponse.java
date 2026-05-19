package com.hoaug.movieapi.modules.system.domain;

import java.time.Instant;
import java.util.List;

public record SystemStatusResponse(
    String overall,
    Instant checkedAt,
    long uptimeSeconds,
    String version,
    List<ComponentStatus> components) {

  public record ComponentStatus(
      String id,
      String name,
      String description,
      String status,
      String detail,
      Long latencyMs) {
  }
}
