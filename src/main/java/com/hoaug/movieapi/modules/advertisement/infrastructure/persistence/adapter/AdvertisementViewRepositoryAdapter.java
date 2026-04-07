package com.hoaug.movieapi.modules.advertisement.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.advertisement.domain.model.AdvertisementView;
import com.hoaug.movieapi.modules.advertisement.domain.repository.AdvertisementViewRepository;
import com.hoaug.movieapi.modules.advertisement.infrastructure.persistence.entity.AdvertisementViewEntity;
import com.hoaug.movieapi.modules.advertisement.infrastructure.persistence.repository.JpaAdvertisementViewRepository;

@Component
public class AdvertisementViewRepositoryAdapter implements AdvertisementViewRepository {

  private final JpaAdvertisementViewRepository jpaAdvertisementViewRepository;

  public AdvertisementViewRepositoryAdapter(
      JpaAdvertisementViewRepository jpaAdvertisementViewRepository) {
    this.jpaAdvertisementViewRepository = jpaAdvertisementViewRepository;
  }

  @Override
  public Optional<AdvertisementView> findById (Long id) {
    return jpaAdvertisementViewRepository.findById(id).map(this::toDomain);
  }

  @Override
  public AdvertisementView save (AdvertisementView advertisementView) {
    AdvertisementViewEntity savedEntity = jpaAdvertisementViewRepository
        .save(toEntity(advertisementView));
    return toDomain(savedEntity);
  }

  @Override
  public List<AdvertisementView> findByUserIdOrderByViewedAtDesc (Long userId) {
    return jpaAdvertisementViewRepository.findByUserIdOrderByViewedAtDesc(userId).stream()
        .map(this::toDomain).toList();
  }

  private AdvertisementView toDomain (AdvertisementViewEntity entity) {
    AdvertisementView advertisementView = new AdvertisementView();
    advertisementView.setId(entity.getId());
    advertisementView.setAdvertisementId(entity.getAdvertisementId());
    advertisementView.setUserId(entity.getUserId());
    advertisementView.setMovieId(entity.getMovieId());
    advertisementView.setEpisodeId(entity.getEpisodeId());
    advertisementView.setViewedAt(entity.getViewedAt());
    advertisementView.setClicked(entity.getClicked());
    advertisementView.setClickedAt(entity.getClickedAt());
    return advertisementView;
  }

  private AdvertisementViewEntity toEntity (AdvertisementView advertisementView) {
    AdvertisementViewEntity entity = new AdvertisementViewEntity();
    entity.setId(advertisementView.getId());
    entity.setAdvertisementId(advertisementView.getAdvertisementId());
    entity.setUserId(advertisementView.getUserId());
    entity.setMovieId(advertisementView.getMovieId());
    entity.setEpisodeId(advertisementView.getEpisodeId());
    entity.setViewedAt(advertisementView.getViewedAt());
    entity.setClicked(advertisementView.getClicked());
    entity.setClickedAt(advertisementView.getClickedAt());
    return entity;
  }
}