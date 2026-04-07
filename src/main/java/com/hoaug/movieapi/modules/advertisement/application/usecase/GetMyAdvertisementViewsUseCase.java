package com.hoaug.movieapi.modules.advertisement.application.usecase;

import java.util.List;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.advertisement.application.dto.response.AdvertisementViewResponse;
import com.hoaug.movieapi.modules.advertisement.application.mapper.AdvertisementMapper;
import com.hoaug.movieapi.modules.advertisement.domain.repository.AdvertisementViewRepository;

@Component
public class GetMyAdvertisementViewsUseCase {

  private final AdvertisementViewRepository advertisementViewRepository;
  private final AdvertisementMapper advertisementMapper;

  public GetMyAdvertisementViewsUseCase(AdvertisementViewRepository advertisementViewRepository,
      AdvertisementMapper advertisementMapper) {
    this.advertisementViewRepository = advertisementViewRepository;
    this.advertisementMapper = advertisementMapper;
  }

  public List<AdvertisementViewResponse> execute (Long userId) {
    return advertisementViewRepository.findByUserIdOrderByViewedAtDesc(userId).stream()
        .map(advertisementMapper::toResponse).toList();
  }
}