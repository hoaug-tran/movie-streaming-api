package com.hoaug.movieapi.modules.chatbot.application;

public class ChatRateLimitException extends ChatbotException {
  public ChatRateLimitException(String message) {
    super(message);
  }
}
