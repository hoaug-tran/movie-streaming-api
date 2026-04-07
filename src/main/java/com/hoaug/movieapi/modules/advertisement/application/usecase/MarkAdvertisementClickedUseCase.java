package com.hoaug.movieapi.modules.advertisement.application.usecase;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.advertisement.application.dto.request.MarkAdvertisementClickedRequest;
import com.hoaug.movieapi.modules.advertisement.application.dto.response.AdvertisementViewResponse;
import com.hoaug.movieapi.modules.advertisement.application.mapper.AdvertisementMapper;
import com.hoaug.movieapi.modules.advertisement.domain.model.AdvertisementView;
import com.hoaug.movieapi.modules.advertisement.domain.repository.AdvertisementViewRepository;

@Component
public class MarkAdvertisementClickedUseCase {

  private final AdvertisementViewRepository advertisementViewRepository;
  private final AdvertisementMapper advertisementMapper;

  public MarkAdvertisementClickedUseCase(AdvertisementViewRepository advertisementViewRepository,
      AdvertisementMapper advertisementMapper) {
    this.advertisementViewRepository = advertisementViewRepository;
    this.advertisementMapper = advertisementMapper;
  }

  public AdvertisementViewResponse execute (MarkAdvertisementClickedRequest request) {
    AdvertisementView advertisementView = advertisementViewRepository
        .findById(request.getAdvertisementViewId())
        .orElseThrow( () -> new AppException(ErrorCode.ADVERTISEMENT_VIEW_NOT_FOUND));

    advertisementView.setClicked(true);
    advertisementView.setClickedAt(LocalDateTime.now());

    AdvertisementView saved = advertisementViewRepository.save(advertisementView);
    return advertisementMapper.toResponse(saved);
  }
}