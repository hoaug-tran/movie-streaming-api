package com.hoaug.movieapi.modules.comment.application.mapper;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.comment.application.dto.response.CommentResponse;
import com.hoaug.movieapi.modules.comment.domain.model.Comment;
import com.hoaug.movieapi.modules.movie.domain.repository.MovieRepository;
import com.hoaug.movieapi.modules.user.domain.repository.UserRepository;

@Component
public class CommentMapper {

  private final MovieRepository movieRepository;
  private final UserRepository userRepository;

  public CommentMapper(MovieRepository movieRepository, UserRepository userRepository) {
    this.movieRepository = movieRepository;
    this.userRepository = userRepository;
  }

  public CommentResponse toResponse (Comment comment) {
    CommentResponse response = new CommentResponse();
    response.setId(comment.getId());
    response.setUserId(comment.getUserId());
    response.setMovieId(comment.getMovieId());
    response.setEpisodeId(comment.getEpisodeId());

    movieRepository.findById(comment.getMovieId()).ifPresent(movie -> {
      response.setMovieSlug(movie.getSlug());
      response.setMovieTitle(movie.getTitle());
    });

    userRepository.findById(comment.getUserId()).ifPresent(user -> {
      response.setAuthorUsername(user.getUsername());
      response.setAuthorFullName(user.getFullName());
      response.setAuthorAvatarUrl(user.getProfilePictureUrl() != null ? user.getProfilePictureUrl() : user.getAvatarUrl());
    });

    response.setParentCommentId(comment.getParentCommentId());
    response.setContent(comment.getContent());
    response.setLikeCount(comment.getLikeCount());
    response.setReplyCount(comment.getReplyCount());
    response.setStatus(comment.getStatus().name());
    response.setCreatedAt(comment.getCreatedAt());
    response.setUpdatedAt(comment.getUpdatedAt());
    return response;
  }
}