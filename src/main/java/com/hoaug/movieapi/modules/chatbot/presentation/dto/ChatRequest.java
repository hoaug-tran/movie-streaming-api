package com.hoaug.movieapi.modules.chatbot.presentation.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChatRequest(
    @NotBlank
    @Size(max = 2000)
    String message,
    List<ChatHistoryItem> history
) {
  public record ChatHistoryItem(String role, String content) {}
}
