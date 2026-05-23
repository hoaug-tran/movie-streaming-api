package com.hoaug.movieapi.modules.movie.application.usecase;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.movie.application.dto.request.CreateTagRequest;
import com.hoaug.movieapi.modules.movie.application.dto.response.TagResponse;
import com.hoaug.movieapi.modules.movie.application.mapper.TagMapper;
import com.hoaug.movieapi.modules.movie.domain.model.Tag;
import com.hoaug.movieapi.modules.movie.domain.repository.TagRepository;

@Component
public class CreateTagUseCase {

  private final TagRepository tagRepository;
  private final TagMapper tagMapper;

  public CreateTagUseCase(TagRepository tagRepository, TagMapper tagMapper) {
    this.tagRepository = tagRepository;
    this.tagMapper = tagMapper;
  }

  @CacheEvict(cacheNames = "tags", allEntries = true)
  public TagResponse execute (CreateTagRequest request) {
    if (tagRepository.existsByNameIgnoreCase(request.getName())
        || tagRepository.existsBySlug(request.getSlug())) {
      throw new AppException(ErrorCode.TAG_EXISTED);
    }

    Tag tag = new Tag();
    tag.setName(request.getName());
    tag.setSlug(request.getSlug());
    tag.setDescription(request.getDescription());

    Tag saved = tagRepository.save(tag);
    return tagMapper.toResponse(saved);
  }
}
