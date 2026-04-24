package com.hoaug.movieapi.modules.movie.application.usecase;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.movie.domain.repository.TagRepository;

@Component
public class DeleteTagUseCase {

  private final TagRepository tagRepository;

  public DeleteTagUseCase(TagRepository tagRepository) {
    this.tagRepository = tagRepository;
  }

  @CacheEvict(cacheNames = "tags", allEntries = true)
  public void execute (Long id) {
    tagRepository.findById(id).orElseThrow( () -> new AppException(ErrorCode.TAG_NOT_FOUND));
    tagRepository.deleteById(id);
  }
}
