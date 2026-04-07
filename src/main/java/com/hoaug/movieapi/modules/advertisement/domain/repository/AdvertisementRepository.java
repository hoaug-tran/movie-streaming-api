package com.hoaug.movieapi.modules.advertisement.domain.repository;

import java.util.List;
import java.util.Optional;

import com.hoaug.movieapi.modules.advertisement.domain.model.Advertisement;
import com.hoaug.movieapi.modules.advertisement.domain.model.AdvertisementType;

public interface AdvertisementRepository {

  Optional<Advertisement> findById (Long id);

  Advertisement save (Advertisement advertisement);

  List<Advertisement> findByIsActiveTrueOrderByPriorityDescCreatedAtDesc ();

  List<Advertisement> findByAdTypeAndIsActiveTrueOrderByPriorityDescCreatedAtDesc (
      AdvertisementType adType);
}