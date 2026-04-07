package com.hoaug.movieapi.modules.advertisement.application.usecase;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.advertisement.application.dto.response.AdvertisementResponse;
import com.hoaug.movieapi.modules.advertisement.application.mapper.AdvertisementMapper;
import com.hoaug.movieapi.modules.advertisement.domain.model.AdvertisementType;
import com.hoaug.movieapi.modules.advertisement.domain.repository.AdvertisementRepository;

@Component
public class GetAvailableAdvertisementsByTypeUseCase {

  private final AdvertisementRepository advertisementRepository;
  private final AdvertisementMapper advertisementMapper;

  public GetAvailableAdvertisementsByTypeUseCase(AdvertisementRepository advertisementRepository,
      AdvertisementMapper advertisementMapper) {
    this.advertisementRepository = advertisementRepository;
    this.advertisementMapper = advertisementMapper;
  }

  public List<AdvertisementResponse> execute (String adType) {
    LocalDateTime now = LocalDateTime.now();

    return advertisementRepository
        .findByAdTypeAndIsActiveTrueOrderByPriorityDescCreatedAtDesc(
            AdvertisementType.valueOf(adType))
        .stream()
        .filter(ad -> (ad.getStartAt() == null || !ad.getStartAt().isAfter(now))
            && (ad.getEndAt() == null || !ad.getEndAt().isBefore(now)))
        .map(advertisementMapper::toResponse).toList();
  }
}