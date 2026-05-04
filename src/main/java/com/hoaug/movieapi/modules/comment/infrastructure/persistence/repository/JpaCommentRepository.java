package com.hoaug.movieapi.modules.comment.infrastructure.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.hoaug.movieapi.modules.comment.domain.model.CommentStatus;
import com.hoaug.movieapi.modules.comment.infrastructure.persistence.entity.CommentEntity;

public interface JpaCommentRepository extends JpaRepository<CommentEntity, Long> {

  List<CommentEntity> findByMovieIdAndParentCommentIdIsNullAndStatusOrderByCreatedAtDesc (
      Long movieId, CommentStatus status);

  List<CommentEntity> findByMovieIdAndStatusOrderByCreatedAtDesc (Long movieId,
      CommentStatus status);

  List<CommentEntity> findByParentCommentIdAndStatusOrderByCreatedAtAsc (Long parentCommentId,
      CommentStatus status);

  @Modifying
  @Query("""
          update CommentEntity c
          set c.replyCount = c.replyCount + 1
          where c.id = :parentCommentId
      """)
  void increaseReplyCount (Long parentCommentId);

  @Modifying
  @Query("""
          update CommentEntity c
          set c.replyCount = case when c.replyCount > 0 then c.replyCount - 1 else 0 end
          where c.id = :parentCommentId
      """)
  void decreaseReplyCount (Long parentCommentId);

  @Modifying
  @Query("""
          update CommentEntity c
          set c.likeCount = c.likeCount + 1
          where c.id = :commentId
      """)
  void increaseLikeCount (Long commentId);

  @Modifying
  @Query("""
          update CommentEntity c
          set c.likeCount = case when c.likeCount > 0 then c.likeCount - 1 else 0 end
          where c.id = :commentId
      """)
  void decreaseLikeCount (Long commentId);

  @Modifying
  @Query("""
          update CommentEntity c
          set c.likeCount = :likeCount
          where c.id = :commentId
      """)
  void updateLikeCount (Long commentId, int likeCount);

  // Discovery Queries
  @Query("SELECT c FROM CommentEntity c WHERE c.status = 'VISIBLE' ORDER BY c.createdAt DESC")
  List<CommentEntity> findNewComments (org.springframework.data.domain.Pageable pageable);

  @Query("SELECT c FROM CommentEntity c WHERE c.status = 'VISIBLE' ORDER BY (c.likeCount + c.replyCount) DESC")
  List<CommentEntity> findTopComments (org.springframework.data.domain.Pageable pageable);

  @Query("SELECT c.movieId FROM CommentEntity c WHERE c.status = 'VISIBLE' GROUP BY c.movieId ORDER BY COUNT(c.id) DESC")
  List<Long> findMostActiveMovieIds (org.springframework.data.domain.Pageable pageable);
}