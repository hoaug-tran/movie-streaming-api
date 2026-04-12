package com.hoaug.movieapi.modules.advertisement.presentation.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.common.response.ResponseUtil;
import com.hoaug.movieapi.modules.advertisement.application.dto.request.CreateAdvertisementRequest;
import com.hoaug.movieapi.modules.advertisement.application.dto.request.CreateAdvertisementViewRequest;
import com.hoaug.movieapi.modules.advertisement.application.dto.request.MarkAdvertisementClickedRequest;
import com.hoaug.movieapi.modules.advertisement.application.dto.response.AdvertisementResponse;
import com.hoaug.movieapi.modules.advertisement.application.dto.response.AdvertisementViewResponse;
import com.hoaug.movieapi.modules.advertisement.application.usecase.CreateAdvertisementUseCase;
import com.hoaug.movieapi.modules.advertisement.application.usecase.CreateAdvertisementViewUseCase;
import com.hoaug.movieapi.modules.advertisement.application.usecase.GetActiveAdvertisementsUseCase;
import com.hoaug.movieapi.modules.advertisement.application.usecase.GetAvailableAdvertisementsByTypeUseCase;
import com.hoaug.movieapi.modules.advertisement.application.usecase.GetMyAdvertisementViewsUseCase;
import com.hoaug.movieapi.modules.advertisement.application.usecase.MarkAdvertisementClickedUseCase;
import com.hoaug.movieapi.modules.auth.domain.repository.AuthUserRepository;
import com.hoaug.movieapi.modules.user.domain.model.User;

import jakarta.validation.Valid;

@RestController
@RequestMapping("${api.prefix:/api/v1}/advertisements")
public class AdvertisementController {

  private final CreateAdvertisementUseCase createAdvertisementUseCase;
  private final GetActiveAdvertisementsUseCase getActiveAdvertisementsUseCase;
  private final GetAvailableAdvertisementsByTypeUseCase getAvailableAdvertisementsByTypeUseCase;
  private final CreateAdvertisementViewUseCase createAdvertisementViewUseCase;
  private final MarkAdvertisementClickedUseCase markAdvertisementClickedUseCase;
  private final GetMyAdvertisementViewsUseCase getMyAdvertisementViewsUseCase;
  private final AuthUserRepository authUserRepository;

  public AdvertisementController(CreateAdvertisementUseCase createAdvertisementUseCase,
      GetActiveAdvertisementsUseCase getActiveAdvertisementsUseCase,
      GetAvailableAdvertisementsByTypeUseCase getAvailableAdvertisementsByTypeUseCase,
      CreateAdvertisementViewUseCase createAdvertisementViewUseCase,
      MarkAdvertisementClickedUseCase markAdvertisementClickedUseCase,
      GetMyAdvertisementViewsUseCase getMyAdvertisementViewsUseCase,
      AuthUserRepository authUserRepository) {
    this.createAdvertisementUseCase = createAdvertisementUseCase;
    this.getActiveAdvertisementsUseCase = getActiveAdvertisementsUseCase;
    this.getAvailableAdvertisementsByTypeUseCase = getAvailableAdvertisementsByTypeUseCase;
    this.createAdvertisementViewUseCase = createAdvertisementViewUseCase;
    this.markAdvertisementClickedUseCase = markAdvertisementClickedUseCase;
    this.getMyAdvertisementViewsUseCase = getMyAdvertisementViewsUseCase;
    this.authUserRepository = authUserRepository;
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping
  public ResponseEntity<AdvertisementResponse> create (
      @Valid @RequestBody CreateAdvertisementRequest request) {
    return ResponseUtil.created(createAdvertisementUseCase.execute(request));
  }

  @GetMapping("/active")
  public ResponseEntity<List<AdvertisementResponse>> getActiveAdvertisements () {
    return ResponseUtil.ok(getActiveAdvertisementsUseCase.execute());
  }

  @GetMapping("/type/{adType}")
  public ResponseEntity<List<AdvertisementResponse>> getByType (@PathVariable String adType) {
    return ResponseUtil.ok(getAvailableAdvertisementsByTypeUseCase.execute(adType));
  }

  @PostMapping("/views")
  public ResponseEntity<AdvertisementViewResponse> createView (Authentication authentication,
      @Valid @RequestBody CreateAdvertisementViewRequest request) {
    return ResponseUtil
        .created(createAdvertisementViewUseCase.execute(getCurrentUserId(authentication), request));
  }

  @PatchMapping("/views/click")
  public ResponseEntity<AdvertisementViewResponse> markClicked (
      @Valid @RequestBody MarkAdvertisementClickedRequest request) {
    return ResponseUtil.ok(markAdvertisementClickedUseCase.execute(request));
  }

  @GetMapping("/views/me")
  public ResponseEntity<List<AdvertisementViewResponse>> getMyViews (
      Authentication authentication) {
    return ResponseUtil
        .ok(getMyAdvertisementViewsUseCase.execute(getCurrentUserId(authentication)));
  }

  private Long getCurrentUserId (Authentication authentication) {
    User user = authUserRepository.findByUsername(authentication.getName())
        .orElseThrow( () -> new AppException(ErrorCode.USER_NOT_FOUND));
    return user.getId();
  }
}