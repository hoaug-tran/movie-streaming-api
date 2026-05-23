package com.hoaug.movieapi.modules.chatbot.application;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ChatbotProperties {

  private final String ollamaBaseUrl;
  private final String ollamaModel;
  private final int ollamaTimeoutSeconds;
  private final int maxHistoryMessages;
  private final int rateLimitPerHour;
  private final int guestRateLimitPerHour;

  public ChatbotProperties(
      @Value("${ollama.base-url:http://localhost:11434}") String ollamaBaseUrl,
      @Value("${ollama.model:qcwind/qwen3-8b-instruct-Q4-K-M:latest}") String ollamaModel,
      @Value("${ollama.timeout-seconds:60}") int ollamaTimeoutSeconds,
      @Value("${chatbot.max-history-messages:10}") int maxHistoryMessages,
      @Value("${chatbot.rate-limit-per-hour:30}") int rateLimitPerHour,
      @Value("${chatbot.guest-rate-limit-per-hour:8}") int guestRateLimitPerHour) {
    this.ollamaBaseUrl = ollamaBaseUrl;
    this.ollamaModel = ollamaModel;
    this.ollamaTimeoutSeconds = ollamaTimeoutSeconds;
    this.maxHistoryMessages = maxHistoryMessages;
    this.rateLimitPerHour = rateLimitPerHour;
    this.guestRateLimitPerHour = guestRateLimitPerHour;
  }

  public String getOllamaBaseUrl() {
    return ollamaBaseUrl;
  }

  public String getOllamaModel() {
    return ollamaModel;
  }

  public int getOllamaTimeoutSeconds() {
    return ollamaTimeoutSeconds;
  }

  public int getMaxHistoryMessages() {
    return maxHistoryMessages;
  }

  public int getRateLimitPerHour() {
    return rateLimitPerHour;
  }

  public int getGuestRateLimitPerHour() {
    return guestRateLimitPerHour;
  }
}
