package com.hoaug.movieapi.modules.chatbot.presentation.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChatRequest(
    @NotBlank
    @Size(max = 800)
    String message,
    @Valid
    @Size(max = 20)
    List<ChatHistoryItem> history
) {
  public record ChatHistoryItem(
      @Size(max = 20)
      String role,
      @Size(max = 800)
      String content
  ) {}
}
