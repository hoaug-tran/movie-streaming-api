package com.hoaug.movieapi.modules.advertisement.application.usecase;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.advertisement.application.dto.request.CreateAdvertisementViewRequest;
import com.hoaug.movieapi.modules.advertisement.application.dto.response.AdvertisementViewResponse;
import com.hoaug.movieapi.modules.advertisement.application.mapper.AdvertisementMapper;
import com.hoaug.movieapi.modules.advertisement.domain.model.AdvertisementView;
import com.hoaug.movieapi.modules.advertisement.domain.repository.AdvertisementRepository;
import com.hoaug.movieapi.modules.advertisement.domain.repository.AdvertisementViewRepository;

@Component
public class CreateAdvertisementViewUseCase {

  private final AdvertisementRepository advertisementRepository;
  private final AdvertisementViewRepository advertisementViewRepository;
  private final AdvertisementMapper advertisementMapper;

  public CreateAdvertisementViewUseCase(AdvertisementRepository advertisementRepository,
      AdvertisementViewRepository advertisementViewRepository,
      AdvertisementMapper advertisementMapper) {
    this.advertisementRepository = advertisementRepository;
    this.advertisementViewRepository = advertisementViewRepository;
    this.advertisementMapper = advertisementMapper;
  }

  public AdvertisementViewResponse execute (Long userId, CreateAdvertisementViewRequest request) {
    advertisementRepository.findById(request.getAdvertisementId())
        .orElseThrow( () -> new AppException(ErrorCode.ADVERTISEMENT_NOT_FOUND));

    AdvertisementView advertisementView = new AdvertisementView();
    advertisementView.setAdvertisementId(request.getAdvertisementId());
    advertisementView.setUserId(userId);
    advertisementView.setMovieId(request.getMovieId());
    advertisementView.setEpisodeId(request.getEpisodeId());
    advertisementView.setViewedAt(LocalDateTime.now());
    advertisementView.setClicked(false);

    AdvertisementView saved = advertisementViewRepository.save(advertisementView);
    return advertisementMapper.toResponse(saved);
  }
}