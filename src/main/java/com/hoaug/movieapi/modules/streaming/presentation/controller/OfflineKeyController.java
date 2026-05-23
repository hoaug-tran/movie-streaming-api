package com.hoaug.movieapi.modules.streaming.presentation.controller;

import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hoaug.movieapi.modules.streaming.application.usecase.GetOfflineHlsKeyUseCase;

@RestController
@RequestMapping("${api.prefix:/api/v1}/stream/offline")
public class OfflineKeyController {

  private final GetOfflineHlsKeyUseCase getOfflineHlsKeyUseCase;

  public OfflineKeyController(GetOfflineHlsKeyUseCase getOfflineHlsKeyUseCase) {
    this.getOfflineHlsKeyUseCase = getOfflineHlsKeyUseCase;
  }

  @GetMapping("/key/{episodeId}/{quality}")
  public ResponseEntity<byte[]> getOfflineKey (@PathVariable Long episodeId,
      @PathVariable String quality, @RequestParam String token) {

    String normalizedQuality = normalizeQuality(quality);
    byte[] key = getOfflineHlsKeyUseCase.execute(episodeId, normalizedQuality, token);

    return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM)
        .cacheControl(CacheControl.noStore()).body(key);
  }

  private String normalizeQuality (String quality) {
    if (quality == null || quality.isBlank())
      return "720p";

    String q = quality.trim().toUpperCase();

    if ("4K".equals(q) || "2160P".equals(q) || "UHD".equals(q)) {
      return "4K";
    }

    if ("1080P".equals(q) || "FHD".equals(q) || "FULL_HD".equals(q)) {
      return "1080p";
    }

    return "720p";
  }
}
