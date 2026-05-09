package com.hoaug.movieapi.modules.review.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.review.domain.model.Review;
import com.hoaug.movieapi.modules.review.domain.repository.ReviewRepository;
import com.hoaug.movieapi.modules.review.infrastructure.persistence.entity.ReviewEntity;
import com.hoaug.movieapi.modules.review.infrastructure.persistence.repository.JpaReviewRepository;

import jakarta.transaction.Transactional;

@Component
public class ReviewRepositoryAdapter implements ReviewRepository {

  private final JpaReviewRepository repo;

  public ReviewRepositoryAdapter(JpaReviewRepository repo) {
    this.repo = repo;
  }

  @Override
  public Optional<Review> findById (Long id) {
    return repo.findById(id).map(this::toDomain);
  }

  @Override
  public Optional<Review> findByUserIdAndMovieId (Long userId, Long movieId) {
    return repo.findByUserIdAndMovieId(userId, movieId).map(this::toDomain);
  }

  @Override
  public List<Review> findByMovieIdOrderByCreatedAtDesc (Long movieId) {
    return repo.findByMovieIdOrderByCreatedAtDesc(movieId).stream().map(this::toDomain).toList();
  }

  @Override
  public Page<Review> findByMovieIdOrderByCreatedAtDesc (Long movieId, Pageable pageable) {
    return repo.findByMovieIdOrderByCreatedAtDesc(movieId, pageable).map(this::toDomain);
  }

  @Override
  public List<Review> findByUserId (Long userId) {
    return repo.findByUserId(userId).stream().map(this::toDomain).toList();
  }

  @Override
  public Review save (Review review) {
    return toDomain(repo.save(toEntity(review)));
  }

  @Override
  public void delete (Review review) {
    repo.delete(toEntity(review));
  }

  @Override
  @Transactional
  public void increaseLikeCount (Long reviewId) {
    repo.increaseLikeCount(reviewId);
  }

  @Override
  @Transactional
  public void decreaseLikeCount (Long reviewId) {
    repo.decreaseLikeCount(reviewId);
  }

  private Review toDomain (ReviewEntity e) {
    Review r = new Review();
    r.setId(e.getId());
    r.setUserId(e.getUserId());
    r.setMovieId(e.getMovieId());
    r.setRating(e.getRating());
    r.setTitle(e.getTitle());
    r.setContent(e.getContent());
    r.setIsEdited(e.getIsEdited());
    r.setStatus(e.getStatus());
    r.setLikeCount(e.getLikeCount());
    r.setCreatedAt(e.getCreatedAt());
    r.setUpdatedAt(e.getUpdatedAt());
    return r;
  }

  private ReviewEntity toEntity (Review r) {
    ReviewEntity e = new ReviewEntity();
    e.setId(r.getId());
    e.setUserId(r.getUserId());
    e.setMovieId(r.getMovieId());
    e.setRating(r.getRating());
    e.setTitle(r.getTitle());
    e.setContent(r.getContent());
    e.setIsEdited(r.getIsEdited());
    e.setStatus(r.getStatus());
    e.setLikeCount(r.getLikeCount());
    e.setCreatedAt(r.getCreatedAt());
    e.setUpdatedAt(r.getUpdatedAt());
    return e;
  }
}