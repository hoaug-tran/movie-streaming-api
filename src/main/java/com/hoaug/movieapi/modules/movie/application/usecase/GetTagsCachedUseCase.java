package com.hoaug.movieapi.modules.movie.application.usecase;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.movie.application.dto.response.TagListResponse;
import com.hoaug.movieapi.modules.movie.application.dto.response.TagResponse;
import com.hoaug.movieapi.modules.movie.application.mapper.TagMapper;
import com.hoaug.movieapi.modules.movie.domain.repository.TagRepository;

@Component
public class GetTagsCachedUseCase {
  private final TagRepository tagRepository;
  private final TagMapper tagMapper;

  public GetTagsCachedUseCase(TagRepository tagRepository, TagMapper tagMapper) {
    this.tagRepository = tagRepository;
    this.tagMapper = tagMapper;
  }

  @Cacheable(value = "tags", unless = "#result == null")
  public TagListResponse execute () {
    List<TagResponse> tags = tagRepository.findAll().stream().map(tagMapper::toResponse).toList();
    return new TagListResponse(tags);
  }
}
