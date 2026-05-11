package com.hoaug.movieapi.modules.advertisement.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.advertisement.domain.model.Advertisement;
import com.hoaug.movieapi.modules.advertisement.domain.model.AdvertisementType;
import com.hoaug.movieapi.modules.advertisement.domain.repository.AdvertisementRepository;
import com.hoaug.movieapi.modules.advertisement.infrastructure.persistence.entity.AdvertisementEntity;
import com.hoaug.movieapi.modules.advertisement.infrastructure.persistence.repository.JpaAdvertisementRepository;

@Component
public class AdvertisementRepositoryAdapter implements AdvertisementRepository {

  private final JpaAdvertisementRepository jpaAdvertisementRepository;

  public AdvertisementRepositoryAdapter(JpaAdvertisementRepository jpaAdvertisementRepository) {
    this.jpaAdvertisementRepository = jpaAdvertisementRepository;
  }

  @Override
  public Optional<Advertisement> findById (Long id) {
    return jpaAdvertisementRepository.findById(id).map(this::toDomain);
  }

  @Override
  public Advertisement save (Advertisement advertisement) {
    AdvertisementEntity savedEntity = jpaAdvertisementRepository.save(toEntity(advertisement));
    return toDomain(savedEntity);
  }

  @Override
  public void deleteById (Long id) {
    jpaAdvertisementRepository.deleteById(id);
  }

  @Override
  public List<Advertisement> findAllOrderByPriorityDescCreatedAtDesc () {
    return jpaAdvertisementRepository.findAllByOrderByPriorityDescCreatedAtDesc().stream()
        .map(this::toDomain).toList();
  }

  @Override
  public List<Advertisement> findByIsActiveTrueOrderByPriorityDescCreatedAtDesc () {
    return jpaAdvertisementRepository.findByIsActiveTrueOrderByPriorityDescCreatedAtDesc().stream()
        .map(this::toDomain).toList();
  }

  @Override
  public List<Advertisement> findByAdTypeAndIsActiveTrueOrderByPriorityDescCreatedAtDesc (
      AdvertisementType adType) {
    return jpaAdvertisementRepository
        .findByAdTypeAndIsActiveTrueOrderByPriorityDescCreatedAtDesc(adType).stream()
        .map(this::toDomain).toList();
  }

  private Advertisement toDomain (AdvertisementEntity entity) {
    Advertisement advertisement = new Advertisement();
    advertisement.setId(entity.getId());
    advertisement.setTitle(entity.getTitle());
    advertisement.setVideoUrl(entity.getVideoUrl());
    advertisement.setTargetUrl(entity.getTargetUrl());
    advertisement.setDurationSeconds(entity.getDurationSeconds());
    advertisement.setAdType(entity.getAdType());
    advertisement.setPriority(entity.getPriority());
    advertisement.setIsSkippable(entity.getIsSkippable());
    advertisement.setSkipAfterSeconds(entity.getSkipAfterSeconds());
    advertisement.setIsActive(entity.getIsActive());
    advertisement.setStartAt(entity.getStartAt());
    advertisement.setEndAt(entity.getEndAt());
    advertisement.setCreatedAt(entity.getCreatedAt());
    advertisement.setUpdatedAt(entity.getUpdatedAt());
    return advertisement;
  }

  private AdvertisementEntity toEntity (Advertisement advertisement) {
    AdvertisementEntity entity = new AdvertisementEntity();
    entity.setId(advertisement.getId());
    entity.setTitle(advertisement.getTitle());
    entity.setVideoUrl(advertisement.getVideoUrl());
    entity.setTargetUrl(advertisement.getTargetUrl());
    entity.setDurationSeconds(advertisement.getDurationSeconds());
    entity.setAdType(advertisement.getAdType());
    entity.setPriority(advertisement.getPriority());
    entity.setIsSkippable(advertisement.getIsSkippable());
    entity.setSkipAfterSeconds(advertisement.getSkipAfterSeconds());
    entity.setIsActive(advertisement.getIsActive());
    entity.setStartAt(advertisement.getStartAt());
    entity.setEndAt(advertisement.getEndAt());
    entity.setCreatedAt(advertisement.getCreatedAt());
    entity.setUpdatedAt(advertisement.getUpdatedAt());
    return entity;
  }
}