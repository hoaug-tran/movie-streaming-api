package com.hoaug.movieapi.modules.review.application.event;

import java.math.BigDecimal;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.event.ReviewCreatedEvent;
import com.hoaug.movieapi.modules.movie.domain.repository.MovieRepository;
import com.hoaug.movieapi.modules.notification.application.dto.request.CreateNotificationRequest;
import com.hoaug.movieapi.modules.notification.application.usecase.CreateNotificationUseCase;

@Component
public class ReviewCreatedEventListener {
  private final MovieRepository movieRepository;
  private final CreateNotificationUseCase createNotificationUseCase;

  public ReviewCreatedEventListener(MovieRepository movieRepository,
      CreateNotificationUseCase createNotificationUseCase) {
    this.movieRepository = movieRepository;
    this.createNotificationUseCase = createNotificationUseCase;
  }

  @EventListener
  public void onReviewCreated (ReviewCreatedEvent event) {
    updateMovieRating(event.getMovieId());
    createNotification(event);
  }

  private void updateMovieRating (Long movieId) {
    var movie = movieRepository.findById(movieId).orElse(null);
    if (movie != null) {
      movie.setAverageRating(BigDecimal.valueOf(calculateAverageRating(movieId)));
      movieRepository.save(movie);
    }
  }

  private Double calculateAverageRating (Long movieId) {
    return 4.5;
  }

  private void createNotification (ReviewCreatedEvent event) {
    CreateNotificationRequest req = new CreateNotificationRequest();
    req.setTitle("New Review Posted");
    req.setType("REVIEW");
    createNotificationUseCase.execute(req);
  }
}
