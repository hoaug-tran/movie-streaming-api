package com.hoaug.movieapi.modules.advertisement.application.usecase;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.advertisement.application.dto.response.AdvertisementResponse;
import com.hoaug.movieapi.modules.advertisement.application.mapper.AdvertisementMapper;
import com.hoaug.movieapi.modules.advertisement.domain.model.Advertisement;
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

  public List<AdvertisementResponse> execute(String adType) {
    LocalDateTime now = LocalDateTime.now();

    List<Advertisement> active = advertisementRepository
        .findByAdTypeAndIsActiveTrueOrderByPriorityDescCreatedAtDesc(
            AdvertisementType.valueOf(adType))
        .stream()
        .filter(ad -> (ad.getStartAt() == null || !ad.getStartAt().isAfter(now))
            && (ad.getEndAt() == null || !ad.getEndAt().isBefore(now)))
        .toList();

    if (active.isEmpty()) {
      return List.of();
    }

    // Group by descending priority and shuffle each priority bucket so campaigns
    // with the same priority rotate fairly instead of always returning in created order.
    List<Advertisement> rotated = new ArrayList<>(active.size());
    int currentPriority = Integer.MIN_VALUE;
    List<Advertisement> bucket = new ArrayList<>();

    List<Advertisement> sorted = new ArrayList<>(active);
    sorted.sort(Comparator.comparingInt(
        (Advertisement a) -> a.getPriority() == null ? 0 : a.getPriority()).reversed());

    for (Advertisement ad : sorted) {
      int priority = ad.getPriority() == null ? 0 : ad.getPriority();
      if (priority != currentPriority) {
        if (!bucket.isEmpty()) {
          Collections.shuffle(bucket);
          rotated.addAll(bucket);
          bucket = new ArrayList<>();
        }
        currentPriority = priority;
      }
      bucket.add(ad);
    }
    if (!bucket.isEmpty()) {
      Collections.shuffle(bucket);
      rotated.addAll(bucket);
    }

    return rotated.stream().map(advertisementMapper::toResponse).toList();
  }
}