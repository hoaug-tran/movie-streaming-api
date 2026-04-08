package com.hoaug.movieapi.modules.movie.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.movie.domain.model.Studio;
import com.hoaug.movieapi.modules.movie.domain.repository.StudioRepository;
import com.hoaug.movieapi.modules.movie.infrastructure.persistence.entity.StudioEntity;
import com.hoaug.movieapi.modules.movie.infrastructure.persistence.repository.JpaStudioRepository;

@Component
public class StudioRepositoryAdapter implements StudioRepository {

  private final JpaStudioRepository jpaStudioRepository;

  public StudioRepositoryAdapter(JpaStudioRepository jpaStudioRepository) {
    this.jpaStudioRepository = jpaStudioRepository;
  }

  @Override
  public List<Studio> findAll () {
    return jpaStudioRepository.findAll().stream().map(entity -> {
      Studio studio = new Studio();
      studio.setId(entity.getId());
      studio.setName(entity.getName());
      studio.setSlug(entity.getSlug());
      studio.setDescription(entity.getDescription());
      studio.setLogoUrl(entity.getLogoUrl());
      studio.setCountry(entity.getCountry());
      studio.setWebsiteUrl(entity.getWebsiteUrl());
      studio.setCreatedAt(entity.getCreatedAt());
      studio.setUpdatedAt(entity.getUpdatedAt());
      return studio;
    }).toList();
  }

  @Override
  public Optional<Studio> findById (Long id) {
    return jpaStudioRepository.findById(id).map(entity -> {
      Studio studio = new Studio();
      studio.setId(entity.getId());
      studio.setName(entity.getName());
      studio.setSlug(entity.getSlug());
      studio.setDescription(entity.getDescription());
      studio.setLogoUrl(entity.getLogoUrl());
      studio.setCountry(entity.getCountry());
      studio.setWebsiteUrl(entity.getWebsiteUrl());
      studio.setCreatedAt(entity.getCreatedAt());
      studio.setUpdatedAt(entity.getUpdatedAt());
      return studio;
    });
  }

  @Override
  public Studio save (Studio studio) {
    StudioEntity entity = new StudioEntity();
    entity.setId(studio.getId());
    entity.setName(studio.getName());
    entity.setSlug(studio.getSlug());
    entity.setDescription(studio.getDescription());
    entity.setLogoUrl(studio.getLogoUrl());
    entity.setCountry(studio.getCountry());
    entity.setWebsiteUrl(studio.getWebsiteUrl());
    entity.setCreatedAt(studio.getCreatedAt());
    entity.setUpdatedAt(studio.getUpdatedAt());

    StudioEntity savedEntity = jpaStudioRepository.save(entity);

    Studio result = new Studio();
    result.setId(savedEntity.getId());
    result.setName(savedEntity.getName());
    result.setSlug(savedEntity.getSlug());
    result.setDescription(savedEntity.getDescription());
    result.setLogoUrl(savedEntity.getLogoUrl());
    result.setCountry(savedEntity.getCountry());
    result.setWebsiteUrl(savedEntity.getWebsiteUrl());
    result.setCreatedAt(savedEntity.getCreatedAt());
    result.setUpdatedAt(savedEntity.getUpdatedAt());
    return result;
  }

  @Override
  public void deleteById (Long id) {
    jpaStudioRepository.deleteById(id);
  }
}
