package com.hoaug.movieapi.modules.chatbot.application;

public record ChatUserContext(boolean authenticated, Long userId, String displayName,
    String subscriptionTier) {

  public static ChatUserContext guest() {
    return new ChatUserContext(false, null, null, null);
  }

  public static ChatUserContext authenticated(Long userId, String displayName, String tier) {
    return new ChatUserContext(true, userId, displayName, tier);
  }
}
