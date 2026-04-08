package com.hoaug.movieapi.modules.movie.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.movie.domain.model.Tag;
import com.hoaug.movieapi.modules.movie.domain.repository.TagRepository;
import com.hoaug.movieapi.modules.movie.infrastructure.persistence.entity.TagEntity;
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
  public Optional<Tag> findById (Long id) {
    return jpaTagRepository.findById(id).map(entity -> {
      Tag tag = new Tag();
      tag.setId(entity.getId());
      tag.setName(entity.getName());
      tag.setSlug(entity.getSlug());
      tag.setDescription(entity.getDescription());
      return tag;
    });
  }

  @Override
  public Tag save (Tag tag) {
    TagEntity entity = new TagEntity();
    entity.setId(tag.getId());
    entity.setName(tag.getName());
    entity.setSlug(tag.getSlug());
    entity.setDescription(tag.getDescription());

    TagEntity savedEntity = jpaTagRepository.save(entity);

    Tag result = new Tag();
    result.setId(savedEntity.getId());
    result.setName(savedEntity.getName());
    result.setSlug(savedEntity.getSlug());
    result.setDescription(savedEntity.getDescription());
    return result;
  }

  @Override
  public void deleteById (Long id) {
    jpaTagRepository.deleteById(id);
  }
}