package com.hoaug.movieapi.modules.advertisement.application.usecase;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.advertisement.application.dto.response.AdvertisementResponse;
import com.hoaug.movieapi.modules.advertisement.application.mapper.AdvertisementMapper;
import com.hoaug.movieapi.modules.advertisement.domain.model.Advertisement;
import com.hoaug.movieapi.modules.advertisement.domain.model.AdvertisementType;
import com.hoaug.movieapi.modules.advertisement.domain.repository.AdvertisementRepository;

@Component
public class GetAvailableAdvertisementsByTypeUseCase {

  private static final Logger log = LoggerFactory.getLogger(GetAvailableAdvertisementsByTypeUseCase.class);

  private final AdvertisementRepository advertisementRepository;
  private final AdvertisementMapper advertisementMapper;

  public GetAvailableAdvertisementsByTypeUseCase(AdvertisementRepository advertisementRepository,
      AdvertisementMapper advertisementMapper) {
    this.advertisementRepository = advertisementRepository;
    this.advertisementMapper = advertisementMapper;
  }

  public List<AdvertisementResponse> execute(String adType) {
    LocalDateTime now = LocalDateTime.now();

    List<Advertisement> active = advertisementRepository
        .findByAdTypeAndIsActiveTrueOrderByPriorityDescCreatedAtDesc(
            AdvertisementType.valueOf(adType))
        .stream()
        .filter(ad -> (ad.getStartAt() == null || !ad.getStartAt().isAfter(now))
            && (ad.getEndAt() == null || !ad.getEndAt().isBefore(now)))
        .toList();

    log.info("[Ads] type={} available after schedule filter: count={} ids={}", adType,
        active.size(), active.stream().map(Advertisement::getId).toList());

    if (active.isEmpty()) {
      return List.of();
    }

    List<Advertisement> rotated = weightedRandomOrder(active);

    log.info("[Ads] type={} weighted order ids={}", adType,
        rotated.stream().map(Advertisement::getId).toList());

    return rotated.stream().map(advertisementMapper::toResponse).toList();
  }

  private List<Advertisement> weightedRandomOrder(List<Advertisement> advertisements) {
    List<Advertisement> remaining = new ArrayList<>(advertisements);
    List<Advertisement> ordered = new ArrayList<>(advertisements.size());

    while (!remaining.isEmpty()) {
      int totalWeight = remaining.stream().mapToInt(this::weightOf).sum();
      int ticket = ThreadLocalRandom.current().nextInt(Math.max(totalWeight, 1));
      int cumulative = 0;
      int selectedIndex = 0;

      for (int i = 0; i < remaining.size(); i++) {
        cumulative += weightOf(remaining.get(i));
        if (ticket < cumulative) {
          selectedIndex = i;
          break;
        }
      }

      ordered.add(remaining.remove(selectedIndex));
    }

    return ordered;
  }

  private int weightOf(Advertisement advertisement) {
    return Math.max(advertisement.getPriority() == null ? 1 : advertisement.getPriority(), 1);
  }
}