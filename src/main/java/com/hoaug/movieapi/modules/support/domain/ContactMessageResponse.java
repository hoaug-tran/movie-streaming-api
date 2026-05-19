package com.hoaug.movieapi.modules.support.domain;

import java.time.Instant;

public record ContactMessageResponse(String ticketId, Instant submittedAt) {
}
