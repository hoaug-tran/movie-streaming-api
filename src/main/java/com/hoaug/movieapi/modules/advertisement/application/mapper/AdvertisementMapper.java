package com.hoaug.movieapi.modules.advertisement.application.mapper;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.advertisement.application.dto.response.AdvertisementResponse;
import com.hoaug.movieapi.modules.advertisement.application.dto.response.AdvertisementViewResponse;
import com.hoaug.movieapi.modules.advertisement.domain.model.Advertisement;
import com.hoaug.movieapi.modules.advertisement.domain.model.AdvertisementView;
import com.hoaug.movieapi.shared.media.MediaUrlResolver;

@Component
public class AdvertisementMapper {
  private final MediaUrlResolver mediaUrlResolver;

  public AdvertisementMapper (MediaUrlResolver mediaUrlResolver) {
    this.mediaUrlResolver = mediaUrlResolver;
  }

  public AdvertisementResponse toResponse (Advertisement advertisement) {
    AdvertisementResponse response = new AdvertisementResponse();
    response.setId(advertisement.getId());
    response.setTitle(advertisement.getTitle());
    response.setVideoUrl(mediaUrlResolver.resolve(advertisement.getVideoUrl()));
    response.setTargetUrl(advertisement.getTargetUrl());
    response.setDurationSeconds(advertisement.getDurationSeconds());
    response.setAdType(advertisement.getAdType().name());
    response.setPriority(advertisement.getPriority());
    response.setIsSkippable(advertisement.getIsSkippable());
    response.setSkipAfterSeconds(advertisement.getSkipAfterSeconds());
    response.setIsActive(advertisement.getIsActive());
    response.setStartAt(advertisement.getStartAt());
    response.setEndAt(advertisement.getEndAt());
    response.setCreatedAt(advertisement.getCreatedAt());
    applyDeliveryStatus(response, advertisement);
    return response;
  }

  public AdvertisementViewResponse toResponse (AdvertisementView advertisementView) {
    AdvertisementViewResponse response = new AdvertisementViewResponse();
    response.setId(advertisementView.getId());
    response.setAdvertisementId(advertisementView.getAdvertisementId());
    response.setUserId(advertisementView.getUserId());
    response.setMovieId(advertisementView.getMovieId());
    response.setEpisodeId(advertisementView.getEpisodeId());
    response.setViewedAt(advertisementView.getViewedAt());
    response.setClicked(advertisementView.getClicked());
    response.setClickedAt(advertisementView.getClickedAt());
    return response;
  }

  private void applyDeliveryStatus(AdvertisementResponse response, Advertisement advertisement) {
    LocalDateTime now = LocalDateTime.now();
    String status;
    String label;
    boolean eligible;

    if (!Boolean.TRUE.equals(advertisement.getIsActive())) {
      status = "PAUSED";
      label = "Tạm dừng";
      eligible = false;
    } else if (advertisement.getStartAt() != null && advertisement.getStartAt().isAfter(now)) {
      status = "SCHEDULED";
      label = "Sắp chạy";
      eligible = false;
    } else if (advertisement.getEndAt() != null && advertisement.getEndAt().isBefore(now)) {
      status = "EXPIRED";
      label = "Hết hạn";
      eligible = false;
    } else {
      status = "RUNNING";
      label = "Đang chạy";
      eligible = true;
    }

    response.setDeliveryStatus(status);
    response.setDeliveryStatusLabel(label);
    response.setEligibleNow(eligible);
  }
}