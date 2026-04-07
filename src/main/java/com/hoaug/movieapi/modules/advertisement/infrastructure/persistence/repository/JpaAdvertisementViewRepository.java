package com.hoaug.movieapi.modules.advertisement.infrastructure.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hoaug.movieapi.modules.advertisement.infrastructure.persistence.entity.AdvertisementViewEntity;

public interface JpaAdvertisementViewRepository
    extends JpaRepository<AdvertisementViewEntity, Long> {

  List<AdvertisementViewEntity> findByUserIdOrderByViewedAtDesc (Long userId);
}