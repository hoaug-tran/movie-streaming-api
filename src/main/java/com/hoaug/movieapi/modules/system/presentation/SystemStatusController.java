package com.hoaug.movieapi.modules.system.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hoaug.movieapi.modules.system.application.SystemStatusService;
import com.hoaug.movieapi.modules.system.domain.SystemStatusResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${api.prefix:/api/v1}/system")
@RequiredArgsConstructor
public class SystemStatusController {

  private final SystemStatusService systemStatusService;

  @GetMapping("/status")
  public ResponseEntity<SystemStatusResponse> getStatus() {
    return ResponseEntity.ok(systemStatusService.check());
  }
}
