package com.hoaug.movieapi.modules.advertisement.application.usecase;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.advertisement.application.dto.request.CreateAdvertisementRequest;
import com.hoaug.movieapi.modules.advertisement.application.dto.response.AdvertisementResponse;
import com.hoaug.movieapi.modules.advertisement.application.mapper.AdvertisementMapper;
import com.hoaug.movieapi.modules.advertisement.domain.model.Advertisement;
import com.hoaug.movieapi.modules.advertisement.domain.model.AdvertisementType;
import com.hoaug.movieapi.modules.advertisement.domain.repository.AdvertisementRepository;

@Component
public class CreateAdvertisementUseCase {

  private final AdvertisementRepository advertisementRepository;
  private final AdvertisementMapper advertisementMapper;

  public CreateAdvertisementUseCase(AdvertisementRepository advertisementRepository,
      AdvertisementMapper advertisementMapper) {
    this.advertisementRepository = advertisementRepository;
    this.advertisementMapper = advertisementMapper;
  }

  public AdvertisementResponse execute (CreateAdvertisementRequest request) {
    Advertisement advertisement = new Advertisement();
    advertisement.setTitle(request.getTitle());
    advertisement.setVideoUrl(request.getVideoUrl());
    advertisement.setTargetUrl(request.getTargetUrl());
    advertisement.setDurationSeconds(request.getDurationSeconds());
    advertisement.setAdType(AdvertisementType.valueOf(request.getAdType()));
    advertisement.setPriority(request.getPriority());
    advertisement.setIsSkippable(request.getIsSkippable());
    advertisement.setSkipAfterSeconds(request.getSkipAfterSeconds());
    advertisement.setIsActive(request.getIsActive());
    advertisement.setStartAt(request.getStartAt());
    advertisement.setEndAt(request.getEndAt());
    advertisement.setCreatedAt(LocalDateTime.now());
    advertisement.setUpdatedAt(LocalDateTime.now());

    Advertisement saved = advertisementRepository.save(advertisement);
    return advertisementMapper.toResponse(saved);
  }
}