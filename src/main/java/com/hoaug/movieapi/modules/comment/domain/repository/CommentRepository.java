package com.hoaug.movieapi.modules.comment.domain.repository;

import java.util.List;
import java.util.Optional;

import com.hoaug.movieapi.modules.comment.domain.model.Comment;

public interface CommentRepository {

  Optional<Comment> findById (Long id);

  List<Comment> findVisibleRootCommentsByMovieIdOrderByCreatedAtDesc (Long movieId);

  List<Comment> findVisibleCommentsByMovieIdOrderByCreatedAtDesc (Long movieId);

  List<Comment> findVisibleRepliesByParentCommentIdOrderByCreatedAtAsc (Long parentCommentId);

  Comment save (Comment comment);

  void delete (Comment comment);

  void increaseReplyCount (Long parentCommentId);

  void decreaseReplyCount (Long parentCommentId);

  void increaseLikeCount (Long commentId);

  void decreaseLikeCount (Long commentId);

  void updateLikeCount (Long commentId, int likeCount);

  List<Comment> findNewComments (int limit);

  List<Comment> findTopComments (int limit);

  List<Long> findMostActiveMovieIds (int limit);
}