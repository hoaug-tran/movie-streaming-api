package com.hoaug.movieapi.modules.commentlike.infrastructure.persistence.adapter;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.commentlike.domain.model.CommentLike;
import com.hoaug.movieapi.modules.commentlike.domain.repository.CommentLikeRepository;
import com.hoaug.movieapi.modules.commentlike.infrastructure.persistence.entity.CommentLikeEntity;
import com.hoaug.movieapi.modules.commentlike.infrastructure.persistence.repository.JpaCommentLikeRepository;

@Component
public class CommentLikeRepositoryAdapter implements CommentLikeRepository {

  private final JpaCommentLikeRepository jpaCommentLikeRepository;

  public CommentLikeRepositoryAdapter(JpaCommentLikeRepository jpaCommentLikeRepository) {
    this.jpaCommentLikeRepository = jpaCommentLikeRepository;
  }

  @Override
  public Optional<CommentLike> findByUserIdAndCommentId (Long userId, Long commentId) {
    return jpaCommentLikeRepository.findByUserIdAndCommentId(userId, commentId).map(this::toDomain);
  }

  @Override
  public CommentLike save (CommentLike commentLike) {
    CommentLikeEntity savedEntity = jpaCommentLikeRepository.save(toEntity(commentLike));
    return toDomain(savedEntity);
  }

  @Override
  public void delete (CommentLike commentLike) {
    jpaCommentLikeRepository.delete(toEntity(commentLike));
  }

  private CommentLike toDomain (CommentLikeEntity entity) {
    CommentLike commentLike = new CommentLike();
    commentLike.setId(entity.getId());
    commentLike.setUserId(entity.getUserId());
    commentLike.setCommentId(entity.getCommentId());
    commentLike.setCreatedAt(entity.getCreatedAt());
    return commentLike;
  }

  private CommentLikeEntity toEntity (CommentLike commentLike) {
    CommentLikeEntity entity = new CommentLikeEntity();
    entity.setId(commentLike.getId());
    entity.setUserId(commentLike.getUserId());
    entity.setCommentId(commentLike.getCommentId());
    entity.setCreatedAt(commentLike.getCreatedAt());
    return entity;
  }
}