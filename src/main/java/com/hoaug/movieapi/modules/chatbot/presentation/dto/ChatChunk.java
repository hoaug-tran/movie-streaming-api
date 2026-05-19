package com.hoaug.movieapi.modules.chatbot.presentation.dto;

public record ChatChunk(String type, String content) {
  public static ChatChunk delta(String content) {
    return new ChatChunk("delta", content);
  }

  public static ChatChunk done() {
    return new ChatChunk("done", "");
  }

  public static ChatChunk error(String message) {
    return new ChatChunk("error", message);
  }

  public static ChatChunk tool(String content) {
    return new ChatChunk("tool", content);
  }
}
