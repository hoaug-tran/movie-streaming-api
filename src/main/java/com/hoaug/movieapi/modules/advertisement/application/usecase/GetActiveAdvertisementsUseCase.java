package com.hoaug.movieapi.modules.advertisement.application.usecase;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.advertisement.application.dto.response.AdvertisementResponse;
import com.hoaug.movieapi.modules.advertisement.application.mapper.AdvertisementMapper;
import com.hoaug.movieapi.modules.advertisement.domain.repository.AdvertisementRepository;

@Component
public class GetActiveAdvertisementsUseCase {

  private final AdvertisementRepository advertisementRepository;
  private final AdvertisementMapper advertisementMapper;

  public GetActiveAdvertisementsUseCase(AdvertisementRepository advertisementRepository,
      AdvertisementMapper advertisementMapper) {
    this.advertisementRepository = advertisementRepository;
    this.advertisementMapper = advertisementMapper;
  }

  public List<AdvertisementResponse> execute () {
    LocalDateTime now = LocalDateTime.now();

    return advertisementRepository.findByIsActiveTrueOrderByPriorityDescCreatedAtDesc().stream()
        .filter(ad -> (ad.getStartAt() == null || !ad.getStartAt().isAfter(now))
            && (ad.getEndAt() == null || !ad.getEndAt().isBefore(now)))
        .map(advertisementMapper::toResponse).toList();
  }
}