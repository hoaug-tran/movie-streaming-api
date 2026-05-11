package com.hoaug.movieapi.modules.advertisement.application.usecase;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.advertisement.domain.repository.AdvertisementRepository;

@Component
public class DeleteAdvertisementUseCase {

  private final AdvertisementRepository advertisementRepository;

  public DeleteAdvertisementUseCase(AdvertisementRepository advertisementRepository) {
    this.advertisementRepository = advertisementRepository;
  }

  public void execute (Long id) {
    if (advertisementRepository.findById(id).isEmpty()) {
      throw new AppException(ErrorCode.ADVERTISEMENT_NOT_FOUND);
    }

    advertisementRepository.deleteById(id);
  }
}
