package com.hoaug.movieapi.modules.review.application.usecase;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.event.EventPublisher;
import com.hoaug.movieapi.common.event.ReviewCreatedEvent;
import com.hoaug.movieapi.modules.review.application.dto.request.UpsertReviewRequest;
import com.hoaug.movieapi.modules.review.application.dto.response.ReviewResponse;
import com.hoaug.movieapi.modules.review.application.mapper.ReviewMapper;
import com.hoaug.movieapi.modules.review.application.validator.ReviewSpamValidator;
import com.hoaug.movieapi.modules.review.domain.model.Review;
import com.hoaug.movieapi.modules.review.domain.repository.ReviewRepository;

@Component
public class UpsertReviewUseCase {

  private final ReviewRepository repo;
  private final ReviewMapper mapper;
  private final ReviewSpamValidator spamValidator;
  private final EventPublisher eventPublisher;

  public UpsertReviewUseCase(ReviewRepository repo, ReviewMapper mapper,
      ReviewSpamValidator spamValidator, EventPublisher eventPublisher) {
    this.repo = repo;
    this.mapper = mapper;
    this.spamValidator = spamValidator;
    this.eventPublisher = eventPublisher;
  }

  public ReviewResponse execute (Long userId, UpsertReviewRequest req) {
    spamValidator.validate(userId, req.getMovieId(), req.getContent(), req.getRating());

    Review review = repo.findByUserIdAndMovieId(userId, req.getMovieId()).orElseGet(Review::new);
    boolean isNew = review.getId() == null;

    if (isNew) {
      review.setUserId(userId);
      review.setCreatedAt(LocalDateTime.now());
    }

    review.setMovieId(req.getMovieId());
    review.setRating(req.getRating());
    review.setContent(req.getContent());
    review.setUpdatedAt(LocalDateTime.now());

    Review saved = repo.save(review);

    if (isNew) {
      eventPublisher.publish(new ReviewCreatedEvent(saved.getId(), saved.getMovieId(),
          saved.getUserId(), saved.getRating()));
    }

    return mapper.toResponse(saved);
  }
}