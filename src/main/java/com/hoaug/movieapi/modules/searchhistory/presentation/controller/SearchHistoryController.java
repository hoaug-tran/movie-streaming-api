package com.hoaug.movieapi.modules.searchhistory.presentation.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.common.response.ResponseUtil;
import com.hoaug.movieapi.modules.auth.domain.repository.AuthUserRepository;
import com.hoaug.movieapi.modules.searchhistory.application.dto.request.CreateSearchHistoryRequest;
import com.hoaug.movieapi.modules.searchhistory.application.dto.response.SearchHistoryResponse;
import com.hoaug.movieapi.modules.searchhistory.application.usecase.ClearMySearchHistoriesUseCase;
import com.hoaug.movieapi.modules.searchhistory.application.usecase.CreateSearchHistoryUseCase;
import com.hoaug.movieapi.modules.searchhistory.application.usecase.DeleteSearchHistoryUseCase;
import com.hoaug.movieapi.modules.searchhistory.application.usecase.GetMyRecentSearchHistoriesUseCase;
import com.hoaug.movieapi.modules.searchhistory.application.usecase.GetMySearchHistoriesUseCase;
import com.hoaug.movieapi.modules.user.domain.model.User;

import jakarta.validation.Valid;

@RestController
@RequestMapping("${api.prefix:/api/v1}/search-histories")
public class SearchHistoryController {

  private final CreateSearchHistoryUseCase createSearchHistoryUseCase;
  private final GetMySearchHistoriesUseCase getMySearchHistoriesUseCase;
  private final GetMyRecentSearchHistoriesUseCase getMyRecentSearchHistoriesUseCase;
  private final DeleteSearchHistoryUseCase deleteSearchHistoryUseCase;
  private final ClearMySearchHistoriesUseCase clearMySearchHistoriesUseCase;
  private final AuthUserRepository authUserRepository;

  public SearchHistoryController(CreateSearchHistoryUseCase createSearchHistoryUseCase,
      GetMySearchHistoriesUseCase getMySearchHistoriesUseCase,
      GetMyRecentSearchHistoriesUseCase getMyRecentSearchHistoriesUseCase,
      DeleteSearchHistoryUseCase deleteSearchHistoryUseCase,
      ClearMySearchHistoriesUseCase clearMySearchHistoriesUseCase,
      AuthUserRepository authUserRepository) {
    this.createSearchHistoryUseCase = createSearchHistoryUseCase;
    this.getMySearchHistoriesUseCase = getMySearchHistoriesUseCase;
    this.getMyRecentSearchHistoriesUseCase = getMyRecentSearchHistoriesUseCase;
    this.deleteSearchHistoryUseCase = deleteSearchHistoryUseCase;
    this.clearMySearchHistoriesUseCase = clearMySearchHistoriesUseCase;
    this.authUserRepository = authUserRepository;
  }

  @PostMapping
  public ResponseEntity<SearchHistoryResponse> create (Authentication authentication,
      @Valid @RequestBody CreateSearchHistoryRequest request) {
    return ResponseUtil
        .created(createSearchHistoryUseCase.execute(getCurrentUserId(authentication), request));
  }

  @GetMapping("/me")
  public ResponseEntity<List<SearchHistoryResponse>> getMySearchHistories (
      Authentication authentication) {
    return ResponseUtil.ok(getMySearchHistoriesUseCase.execute(getCurrentUserId(authentication)));
  }

  @GetMapping("/me/recent")
  public ResponseEntity<List<SearchHistoryResponse>> getMyRecentSearchHistories (
      Authentication authentication, @RequestParam(name = "limit", required = false) Integer limit) {
    return ResponseUtil.ok(
        getMyRecentSearchHistoriesUseCase.execute(getCurrentUserId(authentication), limit));
  }

  @DeleteMapping("/{searchHistoryId}")
  public ResponseEntity<Void> delete (Authentication authentication,
      @PathVariable Long searchHistoryId) {
    deleteSearchHistoryUseCase.execute(getCurrentUserId(authentication), searchHistoryId);
    return ResponseUtil.noContent();
  }

  @DeleteMapping("/me")
  public ResponseEntity<Void> clear (Authentication authentication) {
    clearMySearchHistoriesUseCase.execute(getCurrentUserId(authentication));
    return ResponseUtil.noContent();
  }

  private Long getCurrentUserId (Authentication authentication) {
    if (authentication == null || authentication.getName() == null) {
      throw new AppException(ErrorCode.UNAUTHORIZED);
    }
    User user = authUserRepository.findByUsername(authentication.getName())
        .orElseThrow( () -> new AppException(ErrorCode.USER_NOT_FOUND));
    return user.getId();
  }
}