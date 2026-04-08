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

import com.hoaug.movieapi.modules.movie.application.dto.request.CreateCategoryRequest;
import com.hoaug.movieapi.modules.movie.application.dto.request.UpdateCategoryRequest;
import com.hoaug.movieapi.modules.movie.application.dto.response.CategoryResponse;
import com.hoaug.movieapi.modules.movie.application.usecase.CreateCategoryUseCase;
import com.hoaug.movieapi.modules.movie.application.usecase.DeleteCategoryUseCase;
import com.hoaug.movieapi.modules.movie.application.usecase.GetAllCategoriesUseCase;
import com.hoaug.movieapi.modules.movie.application.usecase.GetCategoryByIdUseCase;
import com.hoaug.movieapi.modules.movie.application.usecase.UpdateCategoryUseCase;

import jakarta.validation.Valid;

@RestController
@RequestMapping("${api.prefix:/api/v1}/admin/categories")
@PreAuthorize("hasRole('ADMIN')")
public class AdminCategoryController {

  private final GetAllCategoriesUseCase getAllCategoriesUseCase;
  private final GetCategoryByIdUseCase getCategoryByIdUseCase;
  private final CreateCategoryUseCase createCategoryUseCase;
  private final UpdateCategoryUseCase updateCategoryUseCase;
  private final DeleteCategoryUseCase deleteCategoryUseCase;

  public AdminCategoryController(GetAllCategoriesUseCase getAllCategoriesUseCase,
      GetCategoryByIdUseCase getCategoryByIdUseCase, CreateCategoryUseCase createCategoryUseCase,
      UpdateCategoryUseCase updateCategoryUseCase, DeleteCategoryUseCase deleteCategoryUseCase) {
    this.getAllCategoriesUseCase = getAllCategoriesUseCase;
    this.getCategoryByIdUseCase = getCategoryByIdUseCase;
    this.createCategoryUseCase = createCategoryUseCase;
    this.updateCategoryUseCase = updateCategoryUseCase;
    this.deleteCategoryUseCase = deleteCategoryUseCase;
  }

  @GetMapping
  public List<CategoryResponse> getAll () {
    return getAllCategoriesUseCase.execute();
  }

  @GetMapping("/{id}")
  public CategoryResponse getById (@PathVariable Long id) {
    return getCategoryByIdUseCase.execute(id);
  }

  @PostMapping
  public CategoryResponse create (@Valid @RequestBody CreateCategoryRequest request) {
    return createCategoryUseCase.execute(request);
  }

  @PutMapping("/{id}")
  public CategoryResponse update (@PathVariable Long id,
      @Valid @RequestBody UpdateCategoryRequest request) {
    return updateCategoryUseCase.execute(id, request);
  }

  @DeleteMapping("/{id}")
  public void delete (@PathVariable Long id) {
    deleteCategoryUseCase.execute(id);
  }
}
