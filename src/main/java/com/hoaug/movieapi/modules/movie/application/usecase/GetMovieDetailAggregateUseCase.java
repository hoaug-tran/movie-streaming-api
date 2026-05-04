package com.hoaug.movieapi.modules.movie.application.usecase;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.comment.application.usecase.GetMovieCommentsUseCase;
import com.hoaug.movieapi.modules.review.application.usecase.GetMovieReviewsUseCase;
import com.hoaug.movieapi.modules.movie.application.dto.response.MovieDetailAggregateResponse;
import com.hoaug.movieapi.modules.movie.application.dto.response.MovieDetailResponse;

@Component
public class GetMovieDetailAggregateUseCase {

  private final GetMovieBySlugUseCase getMovieBySlugUseCase;
  private final GetMovieCommentsUseCase getMovieCommentsUseCase;
  private final GetMovieReviewsUseCase getMovieReviewsUseCase;

  public GetMovieDetailAggregateUseCase(GetMovieBySlugUseCase getMovieBySlugUseCase,
      GetMovieCommentsUseCase getMovieCommentsUseCase, GetMovieReviewsUseCase getMovieReviewsUseCase) {
    this.getMovieBySlugUseCase = getMovieBySlugUseCase;
    this.getMovieCommentsUseCase = getMovieCommentsUseCase;
    this.getMovieReviewsUseCase = getMovieReviewsUseCase;
  }

  public MovieDetailAggregateResponse execute (String slug) {
    MovieDetailResponse movie = getMovieBySlugUseCase.execute(slug);
    MovieDetailAggregateResponse response = new MovieDetailAggregateResponse();
    response.setMovie(movie);
    response.setComments(getMovieCommentsUseCase.execute(movie.getId(), null));
    response.setReviews(getMovieReviewsUseCase.execute(movie.getId()));
    return response;
  }
}
