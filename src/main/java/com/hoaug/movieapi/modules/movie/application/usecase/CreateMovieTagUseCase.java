package com.hoaug.movieapi.modules.movie.application.usecase;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.movie.application.dto.request.CreateMovieTagRequest;
import com.hoaug.movieapi.modules.movie.application.dto.response.TagResponse;
import com.hoaug.movieapi.modules.movie.application.mapper.TagMapper;
import com.hoaug.movieapi.modules.movie.domain.repository.TagRepository;
import com.hoaug.movieapi.modules.movie.infrastructure.persistence.entity.MovieTagEntity;
import com.hoaug.movieapi.modules.movie.infrastructure.persistence.repository.JpaMovieTagRepository;

@Component
public class CreateMovieTagUseCase {

  private final JpaMovieTagRepository jpaMovieTagRepository;
  private final TagRepository tagRepository;
  private final TagMapper tagMapper;

  public CreateMovieTagUseCase(JpaMovieTagRepository jpaMovieTagRepository,
      TagRepository tagRepository, TagMapper tagMapper) {
    this.jpaMovieTagRepository = jpaMovieTagRepository;
    this.tagRepository = tagRepository;
    this.tagMapper = tagMapper;
  }

  public TagResponse execute (Long movieId, CreateMovieTagRequest request) {
    MovieTagEntity entity = new MovieTagEntity();
    entity.setMovieId(movieId);
    entity.setTagId(request.getTagId());

    jpaMovieTagRepository.save(entity);

    var tag = tagRepository.findById(request.getTagId()).get();
    return tagMapper.toResponse(tag);
  }
}
