package com.hoaug.movieapi.modules.review.application.mapper;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.review.application.dto.response.ReviewResponse;
import com.hoaug.movieapi.modules.review.domain.model.Review;

@Component
public class ReviewMapper {

  public ReviewResponse toResponse (Review review) {
    ReviewResponse res = new ReviewResponse();
    res.setId(review.getId());
    res.setMovieId(review.getMovieId());
    res.setRating(review.getRating());
    res.setTitle(review.getTitle());
    res.setContent(review.getContent());
    res.setIsEdited(review.getIsEdited());
    res.setStatus(review.getStatus());
    res.setLikeCount(review.getLikeCount());
    res.setCreatedAt(review.getCreatedAt());
    return res;
  }
}