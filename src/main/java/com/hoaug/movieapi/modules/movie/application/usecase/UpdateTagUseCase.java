package com.hoaug.movieapi.modules.movie.application.usecase;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.movie.application.dto.request.UpdateTagRequest;
import com.hoaug.movieapi.modules.movie.application.dto.response.TagResponse;
import com.hoaug.movieapi.modules.movie.application.mapper.TagMapper;
import com.hoaug.movieapi.modules.movie.domain.model.Tag;
import com.hoaug.movieapi.modules.movie.domain.repository.TagRepository;

@Component
public class UpdateTagUseCase {

  private final TagRepository tagRepository;
  private final TagMapper tagMapper;

  public UpdateTagUseCase(TagRepository tagRepository, TagMapper tagMapper) {
    this.tagRepository = tagRepository;
    this.tagMapper = tagMapper;
  }

  @CacheEvict(cacheNames = "tags", allEntries = true)
  public TagResponse execute (Long id, UpdateTagRequest request) {
    Tag tag = tagRepository.findById(id)
        .orElseThrow( () -> new AppException(ErrorCode.TAG_NOT_FOUND));

    tag.setName(request.getName());
    tag.setSlug(request.getSlug());
    tag.setDescription(request.getDescription());

    Tag saved = tagRepository.save(tag);
    return tagMapper.toResponse(saved);
  }
}
