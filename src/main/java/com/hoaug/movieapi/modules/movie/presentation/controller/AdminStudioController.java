package com.hoaug.movieapi.modules.movie.presentation.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hoaug.movieapi.common.response.ResponseUtil;
import com.hoaug.movieapi.modules.movie.application.dto.request.CreateStudioRequest;
import com.hoaug.movieapi.modules.movie.application.dto.request.UpdateStudioRequest;
import com.hoaug.movieapi.modules.movie.application.dto.response.StudioResponse;
import com.hoaug.movieapi.modules.movie.application.usecase.CreateStudioUseCase;
import com.hoaug.movieapi.modules.movie.application.usecase.DeleteStudioUseCase;
import com.hoaug.movieapi.modules.movie.application.usecase.GetAllStudiosUseCase;
import com.hoaug.movieapi.modules.movie.application.usecase.GetStudioByIdUseCase;
import com.hoaug.movieapi.modules.movie.application.usecase.UpdateStudioUseCase;

import jakarta.validation.Valid;

@RestController
@RequestMapping("${api.prefix:/api/v1}/admin/studios")
@PreAuthorize("hasRole('ADMIN')")
public class AdminStudioController {

  private final GetAllStudiosUseCase getAllStudiosUseCase;
  private final GetStudioByIdUseCase getStudioByIdUseCase;
  private final CreateStudioUseCase createStudioUseCase;
  private final UpdateStudioUseCase updateStudioUseCase;
  private final DeleteStudioUseCase deleteStudioUseCase;

  public AdminStudioController(GetAllStudiosUseCase getAllStudiosUseCase,
      GetStudioByIdUseCase getStudioByIdUseCase, CreateStudioUseCase createStudioUseCase,
      UpdateStudioUseCase updateStudioUseCase, DeleteStudioUseCase deleteStudioUseCase) {
    this.getAllStudiosUseCase = getAllStudiosUseCase;
    this.getStudioByIdUseCase = getStudioByIdUseCase;
    this.createStudioUseCase = createStudioUseCase;
    this.updateStudioUseCase = updateStudioUseCase;
    this.deleteStudioUseCase = deleteStudioUseCase;
  }

  @GetMapping
  public ResponseEntity<List<StudioResponse>> getAll () {
    return ResponseUtil.ok(getAllStudiosUseCase.execute());
  }

  @GetMapping("/{id}")
  public ResponseEntity<StudioResponse> getById (@PathVariable Long id) {
    return ResponseUtil.ok(getStudioByIdUseCase.execute(id));
  }

  @PostMapping
  public ResponseEntity<StudioResponse> create (@Valid @RequestBody CreateStudioRequest request) {
    return ResponseUtil.created(createStudioUseCase.execute(request));
  }

  @PutMapping("/{id}")
  public ResponseEntity<StudioResponse> update (@PathVariable Long id,
      @Valid @RequestBody UpdateStudioRequest request) {
    return ResponseUtil.ok(updateStudioUseCase.execute(id, request));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete (@PathVariable Long id) {
    deleteStudioUseCase.execute(id);
    return ResponseUtil.noContent();
  }
}
