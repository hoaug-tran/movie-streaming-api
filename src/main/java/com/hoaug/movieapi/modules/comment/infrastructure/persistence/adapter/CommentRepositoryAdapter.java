package com.hoaug.movieapi.modules.comment.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.comment.domain.model.Comment;
import com.hoaug.movieapi.modules.comment.domain.model.CommentStatus;
import com.hoaug.movieapi.modules.comment.domain.repository.CommentRepository;
import com.hoaug.movieapi.modules.comment.infrastructure.persistence.entity.CommentEntity;
import com.hoaug.movieapi.modules.comment.infrastructure.persistence.repository.JpaCommentRepository;

import jakarta.transaction.Transactional;

@Component
public class CommentRepositoryAdapter implements CommentRepository {

  private final JpaCommentRepository jpaCommentRepository;

  public CommentRepositoryAdapter(JpaCommentRepository jpaCommentRepository) {
    this.jpaCommentRepository = jpaCommentRepository;
  }

  @Override
  public Optional<Comment> findById (Long id) {
    return jpaCommentRepository.findById(id).map(this::toDomain);
  }

  @Override
  public List<Comment> findVisibleRootCommentsByMovieIdOrderByCreatedAtDesc (Long movieId) {
    return jpaCommentRepository
        .findByMovieIdAndParentCommentIdIsNullAndStatusOrderByCreatedAtDesc(movieId,
            CommentStatus.VISIBLE)
        .stream().map(this::toDomain).toList();
  }

  @Override
  public List<Comment> findVisibleRepliesByParentCommentIdOrderByCreatedAtAsc (
      Long parentCommentId) {
    return jpaCommentRepository
        .findByParentCommentIdAndStatusOrderByCreatedAtAsc(parentCommentId, CommentStatus.VISIBLE)
        .stream().map(this::toDomain).toList();
  }

  @Override
  public Comment save (Comment comment) {
    CommentEntity savedEntity = jpaCommentRepository.save(toEntity(comment));
    return toDomain(savedEntity);
  }

  @Override
  @Transactional
  public void increaseReplyCount (Long parentCommentId) {
    jpaCommentRepository.increaseReplyCount(parentCommentId);
  }

  @Override
  @Transactional
  public void decreaseReplyCount (Long parentCommentId) {
    jpaCommentRepository.decreaseReplyCount(parentCommentId);
  }

  @Override
  @Transactional
  public void increaseLikeCount (Long commentId) {
    jpaCommentRepository.increaseLikeCount(commentId);
  }

  @Override
  @Transactional
  public void decreaseLikeCount (Long commentId) {
    jpaCommentRepository.decreaseLikeCount(commentId);
  }

  private Comment toDomain (CommentEntity entity) {
    Comment comment = new Comment();
    comment.setId(entity.getId());
    comment.setUserId(entity.getUserId());
    comment.setMovieId(entity.getMovieId());
    comment.setParentCommentId(entity.getParentCommentId());
    comment.setContent(entity.getContent());
    comment.setLikeCount(entity.getLikeCount());
    comment.setReplyCount(entity.getReplyCount());
    comment.setStatus(entity.getStatus());
    comment.setCreatedAt(entity.getCreatedAt());
    comment.setUpdatedAt(entity.getUpdatedAt());
    return comment;
  }

  private CommentEntity toEntity (Comment comment) {
    CommentEntity entity = new CommentEntity();
    entity.setId(comment.getId());
    entity.setUserId(comment.getUserId());
    entity.setMovieId(comment.getMovieId());
    entity.setParentCommentId(comment.getParentCommentId());
    entity.setContent(comment.getContent());
    entity.setLikeCount(comment.getLikeCount());
    entity.setReplyCount(comment.getReplyCount());
    entity.setStatus(comment.getStatus());
    entity.setCreatedAt(comment.getCreatedAt());
    entity.setUpdatedAt(comment.getUpdatedAt());
    return entity;
  }
}