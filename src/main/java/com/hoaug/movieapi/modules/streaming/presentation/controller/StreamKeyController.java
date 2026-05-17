package com.hoaug.movieapi.modules.streaming.presentation.controller;

import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.auth.domain.repository.AuthUserRepository;
import com.hoaug.movieapi.modules.streaming.application.service.SubscriptionAccessService;
import com.hoaug.movieapi.modules.streaming.application.usecase.GetAdvertisementHlsKeyUseCase;
import com.hoaug.movieapi.modules.streaming.application.usecase.GetEpisodeHlsKeyUseCase;
import com.hoaug.movieapi.modules.user.domain.model.User;

@RestController
@RequestMapping("${api.prefix:/api/v1}/stream/keys")
public class StreamKeyController {

  private final GetEpisodeHlsKeyUseCase getEpisodeHlsKeyUseCase;
  private final GetAdvertisementHlsKeyUseCase getAdvertisementHlsKeyUseCase;
  private final AuthUserRepository authUserRepository;
  private final SubscriptionAccessService subscriptionAccessService;

  public StreamKeyController (GetEpisodeHlsKeyUseCase getEpisodeHlsKeyUseCase,
      GetAdvertisementHlsKeyUseCase getAdvertisementHlsKeyUseCase,
      AuthUserRepository authUserRepository,
      SubscriptionAccessService subscriptionAccessService) {
    this.getEpisodeHlsKeyUseCase = getEpisodeHlsKeyUseCase;
    this.getAdvertisementHlsKeyUseCase = getAdvertisementHlsKeyUseCase;
    this.authUserRepository = authUserRepository;
    this.subscriptionAccessService = subscriptionAccessService;
  }

  @GetMapping("/series/episodes/{episodeId}/{quality}")
  public ResponseEntity<byte[]> getEpisodeKey (@PathVariable Long episodeId,
      @PathVariable String quality, Authentication authentication) {
    if (!"720p".equals(quality)) {
      if (authentication == null || !authentication.isAuthenticated()
          || "anonymousUser".equals(authentication.getPrincipal())) {
        throw new AppException(ErrorCode.FORBIDDEN);
      }
      User user = authUserRepository.findByUsername(authentication.getName())
          .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
      if (!subscriptionAccessService.canAccessQuality(user.getId(), quality)) {
        throw new AppException(ErrorCode.FORBIDDEN);
      }
    }
    return keyResponse(getEpisodeHlsKeyUseCase.execute(episodeId, quality));
  }

  @GetMapping("/ads/advertisements/{advertisementId}")
  public ResponseEntity<byte[]> getAdvertisementKey (@PathVariable Long advertisementId) {
    return keyResponse(getAdvertisementHlsKeyUseCase.execute(advertisementId));
  }

  private ResponseEntity<byte[]> keyResponse (byte[] key) {
    return ResponseEntity.ok()
        .contentType(MediaType.APPLICATION_OCTET_STREAM)
        .cacheControl(CacheControl.noStore())
        .body(key);
  }
}
