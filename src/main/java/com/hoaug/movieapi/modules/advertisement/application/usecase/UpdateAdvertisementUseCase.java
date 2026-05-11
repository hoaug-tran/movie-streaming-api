package com.hoaug.movieapi.modules.advertisement.application.usecase;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.advertisement.application.dto.request.UpdateAdvertisementRequest;
import com.hoaug.movieapi.modules.advertisement.application.dto.response.AdvertisementResponse;
import com.hoaug.movieapi.modules.advertisement.application.mapper.AdvertisementMapper;
import com.hoaug.movieapi.modules.advertisement.domain.model.Advertisement;
import com.hoaug.movieapi.modules.advertisement.domain.model.AdvertisementType;
import com.hoaug.movieapi.modules.advertisement.domain.repository.AdvertisementRepository;

@Component
public class UpdateAdvertisementUseCase {

  private final AdvertisementRepository advertisementRepository;
  private final AdvertisementMapper advertisementMapper;

  public UpdateAdvertisementUseCase(AdvertisementRepository advertisementRepository,
      AdvertisementMapper advertisementMapper) {
    this.advertisementRepository = advertisementRepository;
    this.advertisementMapper = advertisementMapper;
  }

  public AdvertisementResponse execute (Long id, UpdateAdvertisementRequest request) {
    Advertisement advertisement = advertisementRepository.findById(id)
        .orElseThrow( () -> new AppException(ErrorCode.ADVERTISEMENT_NOT_FOUND));

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
    advertisement.setUpdatedAt(LocalDateTime.now());

    return advertisementMapper.toResponse(advertisementRepository.save(advertisement));
  }
}
