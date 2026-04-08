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

import com.hoaug.movieapi.modules.movie.application.dto.request.CreateTagRequest;
import com.hoaug.movieapi.modules.movie.application.dto.request.UpdateTagRequest;
import com.hoaug.movieapi.modules.movie.application.dto.response.TagResponse;
import com.hoaug.movieapi.modules.movie.application.usecase.CreateTagUseCase;
import com.hoaug.movieapi.modules.movie.application.usecase.DeleteTagUseCase;
import com.hoaug.movieapi.modules.movie.application.usecase.GetAllTagsUseCase;
import com.hoaug.movieapi.modules.movie.application.usecase.GetTagByIdUseCase;
import com.hoaug.movieapi.modules.movie.application.usecase.UpdateTagUseCase;

import jakarta.validation.Valid;

@RestController
@RequestMapping("${api.prefix:/api/v1}/admin/tags")
@PreAuthorize("hasRole('ADMIN')")
public class AdminTagController {

  private final GetAllTagsUseCase getAllTagsUseCase;
  private final GetTagByIdUseCase getTagByIdUseCase;
  private final CreateTagUseCase createTagUseCase;
  private final UpdateTagUseCase updateTagUseCase;
  private final DeleteTagUseCase deleteTagUseCase;

  public AdminTagController(GetAllTagsUseCase getAllTagsUseCase,
      GetTagByIdUseCase getTagByIdUseCase, CreateTagUseCase createTagUseCase,
      UpdateTagUseCase updateTagUseCase, DeleteTagUseCase deleteTagUseCase) {
    this.getAllTagsUseCase = getAllTagsUseCase;
    this.getTagByIdUseCase = getTagByIdUseCase;
    this.createTagUseCase = createTagUseCase;
    this.updateTagUseCase = updateTagUseCase;
    this.deleteTagUseCase = deleteTagUseCase;
  }

  @GetMapping
  public List<TagResponse> getAll () {
    return getAllTagsUseCase.execute();
  }

  @GetMapping("/{id}")
  public TagResponse getById (@PathVariable Long id) {
    return getTagByIdUseCase.execute(id);
  }

  @PostMapping
  public TagResponse create (@Valid @RequestBody CreateTagRequest request) {
    return createTagUseCase.execute(request);
  }

  @PutMapping("/{id}")
  public TagResponse update (@PathVariable Long id, @Valid @RequestBody UpdateTagRequest request) {
    return updateTagUseCase.execute(id, request);
  }

  @DeleteMapping("/{id}")
  public void delete (@PathVariable Long id) {
    deleteTagUseCase.execute(id);
  }
}
