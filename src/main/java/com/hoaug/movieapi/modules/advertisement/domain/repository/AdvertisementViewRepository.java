package com.hoaug.movieapi.modules.advertisement.domain.repository;

import java.util.List;
import java.util.Optional;

import com.hoaug.movieapi.modules.advertisement.domain.model.AdvertisementView;

public interface AdvertisementViewRepository {

  Optional<AdvertisementView> findById (Long id);

  AdvertisementView save (AdvertisementView advertisementView);

  List<AdvertisementView> findByUserIdOrderByViewedAtDesc (Long userId);
}