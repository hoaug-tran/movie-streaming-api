package com.hoaug.movieapi.modules.devicesession.presentation.controller;

import java.util.List;

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
import com.hoaug.movieapi.modules.auth.domain.repository.AuthUserRepository;
import com.hoaug.movieapi.modules.devicesession.application.dto.request.CreateDeviceSessionRequest;
import com.hoaug.movieapi.modules.devicesession.application.dto.response.DeviceSessionResponse;
import com.hoaug.movieapi.modules.devicesession.application.usecase.CountActiveDeviceSessionsUseCase;
import com.hoaug.movieapi.modules.devicesession.application.usecase.CreateDeviceSessionUseCase;
import com.hoaug.movieapi.modules.devicesession.application.usecase.GetMyDeviceSessionsUseCase;
import com.hoaug.movieapi.modules.devicesession.application.usecase.RevokeAllMyDeviceSessionsUseCase;
import com.hoaug.movieapi.modules.devicesession.application.usecase.RevokeDeviceSessionUseCase;
import com.hoaug.movieapi.modules.devicesession.application.usecase.UpdateDeviceSessionActivityUseCase;
import com.hoaug.movieapi.modules.user.domain.model.User;

import jakarta.validation.Valid;

@RestController
@RequestMapping("${api.prefix:/api/v1}/device-sessions")
public class DeviceSessionController {

  private final CreateDeviceSessionUseCase createDeviceSessionUseCase;
  private final GetMyDeviceSessionsUseCase getMyDeviceSessionsUseCase;
  private final UpdateDeviceSessionActivityUseCase updateDeviceSessionActivityUseCase;
  private final RevokeDeviceSessionUseCase revokeDeviceSessionUseCase;
  private final RevokeAllMyDeviceSessionsUseCase revokeAllMyDeviceSessionsUseCase;
  private final CountActiveDeviceSessionsUseCase countActiveDeviceSessionsUseCase;
  private final AuthUserRepository authUserRepository;

  public DeviceSessionController(CreateDeviceSessionUseCase createDeviceSessionUseCase,
      GetMyDeviceSessionsUseCase getMyDeviceSessionsUseCase,
      UpdateDeviceSessionActivityUseCase updateDeviceSessionActivityUseCase,
      RevokeDeviceSessionUseCase revokeDeviceSessionUseCase,
      RevokeAllMyDeviceSessionsUseCase revokeAllMyDeviceSessionsUseCase,
      CountActiveDeviceSessionsUseCase countActiveDeviceSessionsUseCase,
      AuthUserRepository authUserRepository) {
    this.createDeviceSessionUseCase = createDeviceSessionUseCase;
    this.getMyDeviceSessionsUseCase = getMyDeviceSessionsUseCase;
    this.updateDeviceSessionActivityUseCase = updateDeviceSessionActivityUseCase;
    this.revokeDeviceSessionUseCase = revokeDeviceSessionUseCase;
    this.revokeAllMyDeviceSessionsUseCase = revokeAllMyDeviceSessionsUseCase;
    this.countActiveDeviceSessionsUseCase = countActiveDeviceSessionsUseCase;
    this.authUserRepository = authUserRepository;
  }

  @PostMapping
  public DeviceSessionResponse create (Authentication authentication,
      @Valid @RequestBody CreateDeviceSessionRequest request) {
    return createDeviceSessionUseCase.execute(getCurrentUserId(authentication), request);
  }

  @GetMapping("/me")
  public List<DeviceSessionResponse> getMySessions (Authentication authentication) {
    return getMyDeviceSessionsUseCase.execute(getCurrentUserId(authentication));
  }

  @PatchMapping("/{sessionId}/active")
  public DeviceSessionResponse updateActivity (Authentication authentication,
      @PathVariable Long sessionId) {
    return updateDeviceSessionActivityUseCase.execute(getCurrentUserId(authentication), sessionId);
  }

  @PatchMapping("/{sessionId}/revoke")
  public void revoke (Authentication authentication, @PathVariable Long sessionId) {
    revokeDeviceSessionUseCase.execute(getCurrentUserId(authentication), sessionId);
  }

  @PatchMapping("/me/revoke-all")
  public void revokeAll (Authentication authentication) {
    revokeAllMyDeviceSessionsUseCase.execute(getCurrentUserId(authentication));
  }

  @GetMapping("/me/active-count")
  public Long countActive (Authentication authentication) {
    return countActiveDeviceSessionsUseCase.execute(getCurrentUserId(authentication));
  }

  private Long getCurrentUserId (Authentication authentication) {
    User user = authUserRepository.findByUsername(authentication.getName())
        .orElseThrow( () -> new AppException(ErrorCode.USER_NOT_FOUND));
    return user.getId();
  }
}