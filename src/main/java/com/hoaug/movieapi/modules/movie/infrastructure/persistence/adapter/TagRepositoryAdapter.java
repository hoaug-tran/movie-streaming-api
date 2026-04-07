package com.hoaug.movieapi.modules.movie.infrastructure.persistence.adapter;

import java.util.List;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.movie.domain.model.Tag;
import com.hoaug.movieapi.modules.movie.domain.repository.TagRepository;
import com.hoaug.movieapi.modules.movie.infrastructure.persistence.repository.JpaTagRepository;

@Component
public class TagRepositoryAdapter implements TagRepository {

  private final JpaTagRepository jpaTagRepository;

  public TagRepositoryAdapter(JpaTagRepository jpaTagRepository) {
    this.jpaTagRepository = jpaTagRepository;
  }

  @Override
  public List<Tag> findAll () {
    return jpaTagRepository.findAll().stream().map(entity -> {
      Tag tag = new Tag();
      tag.setId(entity.getId());
      tag.setName(entity.getName());
      tag.setSlug(entity.getSlug());
      tag.setDescription(entity.getDescription());
      return tag;
    }).toList();
  }

  @Override
  public Tag findById (Long id) {
    return jpaTagRepository.findById(id).map(entity -> {
      Tag tag = new Tag();
      tag.setId(entity.getId());
      tag.setName(entity.getName());
      tag.setSlug(entity.getSlug());
      tag.setDescription(entity.getDescription());
      return tag;
    }).orElse(null);
  }
}