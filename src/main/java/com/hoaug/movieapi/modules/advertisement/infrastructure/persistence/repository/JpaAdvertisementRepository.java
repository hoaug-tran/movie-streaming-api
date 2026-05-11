package com.hoaug.movieapi.modules.advertisement.infrastructure.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hoaug.movieapi.modules.advertisement.domain.model.AdvertisementType;
import com.hoaug.movieapi.modules.advertisement.infrastructure.persistence.entity.AdvertisementEntity;

public interface JpaAdvertisementRepository extends JpaRepository<AdvertisementEntity, Long> {

  List<AdvertisementEntity> findAllByOrderByPriorityDescCreatedAtDesc ();

  List<AdvertisementEntity> findByIsActiveTrueOrderByPriorityDescCreatedAtDesc ();

  List<AdvertisementEntity> findByAdTypeAndIsActiveTrueOrderByPriorityDescCreatedAtDesc (
      AdvertisementType adType);

  List<AdvertisementEntity> findTop5ByIsActiveTrueOrderByPriorityDescCreatedAtDesc ();
}