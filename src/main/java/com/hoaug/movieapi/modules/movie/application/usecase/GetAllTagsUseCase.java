package com.hoaug.movieapi.modules.movie.application.usecase;

import java.util.List;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.movie.application.dto.response.TagResponse;
import com.hoaug.movieapi.modules.movie.application.mapper.TagMapper;
import com.hoaug.movieapi.modules.movie.domain.model.Tag;
import com.hoaug.movieapi.modules.movie.domain.repository.TagRepository;

@Component
public class GetAllTagsUseCase {

  private final TagRepository tagRepository;
  private final TagMapper tagMapper;

  public GetAllTagsUseCase(TagRepository tagRepository, TagMapper tagMapper) {
    this.tagRepository = tagRepository;
    this.tagMapper = tagMapper;
  }

  public List<TagResponse> execute () {
    List<Tag> tags = tagRepository.findAll();
    return tags.stream().map(tagMapper::toResponse).toList();
  }
}
