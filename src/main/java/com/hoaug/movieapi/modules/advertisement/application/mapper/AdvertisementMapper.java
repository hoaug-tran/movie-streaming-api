package com.hoaug.movieapi.modules.advertisement.application.mapper;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.advertisement.application.dto.response.AdvertisementResponse;
import com.hoaug.movieapi.modules.advertisement.application.dto.response.AdvertisementViewResponse;
import com.hoaug.movieapi.modules.advertisement.domain.model.Advertisement;
import com.hoaug.movieapi.modules.advertisement.domain.model.AdvertisementView;

@Component
public class AdvertisementMapper {

  public AdvertisementResponse toResponse (Advertisement advertisement) {
    AdvertisementResponse response = new AdvertisementResponse();
    response.setId(advertisement.getId());
    response.setTitle(advertisement.getTitle());
    response.setVideoUrl(advertisement.getVideoUrl());
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
}