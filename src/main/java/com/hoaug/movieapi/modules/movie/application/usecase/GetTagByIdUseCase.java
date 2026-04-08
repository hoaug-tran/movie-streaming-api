package com.hoaug.movieapi.modules.movie.application.usecase;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.movie.application.dto.response.TagResponse;
import com.hoaug.movieapi.modules.movie.application.mapper.TagMapper;
import com.hoaug.movieapi.modules.movie.domain.model.Tag;
import com.hoaug.movieapi.modules.movie.domain.repository.TagRepository;

@Component
public class GetTagByIdUseCase {

  private final TagRepository tagRepository;
  private final TagMapper tagMapper;

  public GetTagByIdUseCase(TagRepository tagRepository, TagMapper tagMapper) {
    this.tagRepository = tagRepository;
    this.tagMapper = tagMapper;
  }

  public TagResponse execute (Long id) {
    Tag tag = tagRepository.findById(id)
        .orElseThrow( () -> new AppException(ErrorCode.TAG_NOT_FOUND));
    return tagMapper.toResponse(tag);
  }
}
