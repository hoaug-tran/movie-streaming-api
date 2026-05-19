package com.hoaug.movieapi.modules.streaming.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.auth.domain.repository.AuthUserRepository;
import com.hoaug.movieapi.modules.streaming.application.usecase.GetOfflinePackageUseCase;
import com.hoaug.movieapi.modules.user.domain.model.User;

@RestController
@RequestMapping("${api.prefix:/api/v1}/stream/offline")
public class OfflinePackageController {

  private final GetOfflinePackageUseCase getOfflinePackageUseCase;
  private final AuthUserRepository authUserRepository;

  public OfflinePackageController (GetOfflinePackageUseCase getOfflinePackageUseCase,
      AuthUserRepository authUserRepository) {
    this.getOfflinePackageUseCase = getOfflinePackageUseCase;
    this.authUserRepository = authUserRepository;
  }

  @GetMapping("/episodes/{episodeId}/{quality}/package")
  public ResponseEntity<GetOfflinePackageUseCase.OfflinePackageResponse> getPackage (
      @PathVariable Long episodeId,
      @PathVariable String quality,
      Authentication authentication) {

    if (authentication == null || !authentication.isAuthenticated()
        || "anonymousUser".equals(authentication.getPrincipal())) {
      throw new AppException(ErrorCode.FORBIDDEN);
    }

    User user = authUserRepository.findByUsername(authentication.getName())
        .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

    return ResponseEntity.ok(getOfflinePackageUseCase.execute(user.getId(), episodeId, quality));
  }
}
