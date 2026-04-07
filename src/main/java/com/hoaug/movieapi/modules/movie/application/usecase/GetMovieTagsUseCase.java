package com.hoaug.movieapi.modules.movie.application.usecase;

import java.util.List;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.movie.application.dto.response.TagResponse;
import com.hoaug.movieapi.modules.movie.application.mapper.TagMapper;
import com.hoaug.movieapi.modules.movie.domain.repository.TagRepository;
import com.hoaug.movieapi.modules.movie.infrastructure.persistence.repository.JpaMovieTagRepository;

@Component
public class GetMovieTagsUseCase {

  private final JpaMovieTagRepository jpaMovieTagRepository;
  private final TagRepository tagRepository;
  private final TagMapper tagMapper;

  public GetMovieTagsUseCase(JpaMovieTagRepository jpaMovieTagRepository,
      TagRepository tagRepository, TagMapper tagMapper) {
    this.jpaMovieTagRepository = jpaMovieTagRepository;
    this.tagRepository = tagRepository;
    this.tagMapper = tagMapper;
  }

  public List<TagResponse> execute (Long movieId) {
    return jpaMovieTagRepository.findByMovieId(movieId).stream().map(entity -> {
      var tag = tagRepository.findById(entity.getTagId());
      return tagMapper.toResponse(tag);
    }).toList();
  }
}
