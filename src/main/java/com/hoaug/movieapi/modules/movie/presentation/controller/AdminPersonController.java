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
import com.hoaug.movieapi.modules.movie.application.dto.request.CreatePersonRequest;
import com.hoaug.movieapi.modules.movie.application.dto.request.UpdatePersonRequest;
import com.hoaug.movieapi.modules.movie.application.dto.response.PersonResponse;
import com.hoaug.movieapi.modules.movie.application.usecase.CreatePersonUseCase;
import com.hoaug.movieapi.modules.movie.application.usecase.DeletePersonUseCase;
import com.hoaug.movieapi.modules.movie.application.usecase.GetAllPersonsUseCase;
import com.hoaug.movieapi.modules.movie.application.usecase.GetPersonByIdUseCase;
import com.hoaug.movieapi.modules.movie.application.usecase.UpdatePersonUseCase;

import jakarta.validation.Valid;

@RestController
@RequestMapping("${api.prefix:/api/v1}/admin/persons")
@PreAuthorize("hasRole('ADMIN')")
public class AdminPersonController {

  private final GetAllPersonsUseCase getAllPersonsUseCase;
  private final GetPersonByIdUseCase getPersonByIdUseCase;
  private final CreatePersonUseCase createPersonUseCase;
  private final UpdatePersonUseCase updatePersonUseCase;
  private final DeletePersonUseCase deletePersonUseCase;

  public AdminPersonController(GetAllPersonsUseCase getAllPersonsUseCase,
      GetPersonByIdUseCase getPersonByIdUseCase, CreatePersonUseCase createPersonUseCase,
      UpdatePersonUseCase updatePersonUseCase, DeletePersonUseCase deletePersonUseCase) {
    this.getAllPersonsUseCase = getAllPersonsUseCase;
    this.getPersonByIdUseCase = getPersonByIdUseCase;
    this.createPersonUseCase = createPersonUseCase;
    this.updatePersonUseCase = updatePersonUseCase;
    this.deletePersonUseCase = deletePersonUseCase;
  }

  @GetMapping
  public ResponseEntity<List<PersonResponse>> getAll () {
    return ResponseUtil.ok(getAllPersonsUseCase.execute());
  }

  @GetMapping("/{id}")
  public ResponseEntity<PersonResponse> getById (@PathVariable Long id) {
    return ResponseUtil.ok(getPersonByIdUseCase.execute(id));
  }

  @PostMapping
  public ResponseEntity<PersonResponse> create (@Valid @RequestBody CreatePersonRequest request) {
    return ResponseUtil.created(createPersonUseCase.execute(request));
  }

  @PutMapping("/{id}")
  public ResponseEntity<PersonResponse> update (@PathVariable Long id,
      @Valid @RequestBody UpdatePersonRequest request) {
    return ResponseUtil.ok(updatePersonUseCase.execute(id, request));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete (@PathVariable Long id) {
    deletePersonUseCase.execute(id);
    return ResponseUtil.noContent();
  }
}
