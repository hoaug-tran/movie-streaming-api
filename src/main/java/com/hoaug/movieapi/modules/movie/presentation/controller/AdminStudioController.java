package com.hoaug.movieapi.modules.movie.presentation.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
  public List<StudioResponse> getAll () {
    return getAllStudiosUseCase.execute();
  }

  @GetMapping("/{id}")
  public StudioResponse getById (@PathVariable Long id) {
    return getStudioByIdUseCase.execute(id);
  }

  @PostMapping
  public StudioResponse create (@Valid @RequestBody CreateStudioRequest request) {
    return createStudioUseCase.execute(request);
  }

  @PutMapping("/{id}")
  public StudioResponse update (@PathVariable Long id,
      @Valid @RequestBody UpdateStudioRequest request) {
    return updateStudioUseCase.execute(id, request);
  }

  @DeleteMapping("/{id}")
  public void delete (@PathVariable Long id) {
    deleteStudioUseCase.execute(id);
  }
}
