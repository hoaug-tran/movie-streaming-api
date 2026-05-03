package com.hoaug.movieapi.modules.streaming.presentation.controller;

import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hoaug.movieapi.modules.streaming.application.usecase.GetAdvertisementHlsKeyUseCase;
import com.hoaug.movieapi.modules.streaming.application.usecase.GetEpisodeHlsKeyUseCase;

@RestController
@RequestMapping("${api.prefix:/api/v1}/stream/keys")
public class StreamKeyController {
  private final GetEpisodeHlsKeyUseCase getEpisodeHlsKeyUseCase;
  private final GetAdvertisementHlsKeyUseCase getAdvertisementHlsKeyUseCase;

  public StreamKeyController(GetEpisodeHlsKeyUseCase getEpisodeHlsKeyUseCase,
      GetAdvertisementHlsKeyUseCase getAdvertisementHlsKeyUseCase) {
    this.getEpisodeHlsKeyUseCase = getEpisodeHlsKeyUseCase;
    this.getAdvertisementHlsKeyUseCase = getAdvertisementHlsKeyUseCase;
  }

  @GetMapping("/series/episodes/{episodeId}")
  public ResponseEntity<byte[]> getEpisodeKey (@PathVariable Long episodeId) {
    byte[] key = getEpisodeHlsKeyUseCase.execute(episodeId);
    return keyResponse(key);
  }

  @GetMapping("/ads/advertisements/{advertisementId}")
  public ResponseEntity<byte[]> getAdvertisementKey (@PathVariable Long advertisementId) {
    byte[] key = getAdvertisementHlsKeyUseCase.execute(advertisementId);
    return keyResponse(key);
  }

  private ResponseEntity<byte[]> keyResponse (byte[] key) {
    return ResponseEntity.ok()
        .contentType(MediaType.APPLICATION_OCTET_STREAM)
        .cacheControl(CacheControl.noStore())
        .body(key);
  }
}
