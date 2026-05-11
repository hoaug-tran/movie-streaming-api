package com.hoaug.movieapi.modules.review.application.usecase;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.event.EventPublisher;
import com.hoaug.movieapi.common.event.ReviewCreatedEvent;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.movie.domain.repository.MovieRepository;
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
  private final ReviewEligibilityUseCase reviewEligibilityUseCase;
  private final MovieRepository movieRepository;

  public UpsertReviewUseCase(ReviewRepository repo, ReviewMapper mapper,
      ReviewSpamValidator spamValidator, EventPublisher eventPublisher,
      ReviewEligibilityUseCase reviewEligibilityUseCase, MovieRepository movieRepository) {
    this.repo = repo;
    this.mapper = mapper;
    this.spamValidator = spamValidator;
    this.eventPublisher = eventPublisher;
    this.reviewEligibilityUseCase = reviewEligibilityUseCase;
    this.movieRepository = movieRepository;
  }

  public ReviewResponse execute (Long userId, UpsertReviewRequest req) {
    var movie = movieRepository.findById(req.getMovieId())
        .orElseThrow( () -> new AppException(ErrorCode.MOVIE_NOT_FOUND));

    if (Boolean.TRUE.equals(movie.getReviewsLocked())) {
      throw new AppException(ErrorCode.MOVIE_REVIEWS_LOCKED);
    }

    reviewEligibilityUseCase.validateCanReview(userId, req.getMovieId());
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