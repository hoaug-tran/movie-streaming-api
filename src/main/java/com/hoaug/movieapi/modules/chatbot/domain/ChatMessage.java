package com.hoaug.movieapi.modules.chatbot.domain;

import java.time.Instant;

public record ChatMessage(ChatRole role, String content, Instant createdAt) {
  public static ChatMessage user(String content) {
    return new ChatMessage(ChatRole.USER, content, Instant.now());
  }

  public static ChatMessage assistant(String content) {
    return new ChatMessage(ChatRole.ASSISTANT, content, Instant.now());
  }

  public static ChatMessage system(String content) {
    return new ChatMessage(ChatRole.SYSTEM, content, Instant.now());
  }

  public static ChatMessage tool(String content) {
    return new ChatMessage(ChatRole.TOOL, content, Instant.now());
  }
}
