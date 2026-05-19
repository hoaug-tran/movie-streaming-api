package com.hoaug.movieapi.modules.chatbot.application;

import java.time.LocalDateTime;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.auth.domain.repository.AuthUserRepository;
import com.hoaug.movieapi.modules.user.domain.model.User;

@Component
public class ChatUserContextProvider {

  private final AuthUserRepository authUserRepository;

  public ChatUserContextProvider(AuthUserRepository authUserRepository) {
    this.authUserRepository = authUserRepository;
  }

  public ChatUserContext resolve(Authentication authentication) {
    if (authentication == null || !authentication.isAuthenticated()
        || "anonymousUser".equals(authentication.getPrincipal())) {
      return ChatUserContext.guest();
    }
    return authUserRepository.findByUsername(authentication.getName())
        .map(this::toContext)
        .orElseGet(ChatUserContext::guest);
  }

  private ChatUserContext toContext(User user) {
    String displayName = user.getFullName();
    if (displayName == null || displayName.isBlank()) {
      displayName = user.getUsername();
    }
    String tier = isPremiumActive(user) ? "Premium" : "Miễn phí";
    return ChatUserContext.authenticated(user.getId(), displayName, tier);
  }

  private boolean isPremiumActive(User user) {
    LocalDateTime expiry = user.getPremiumExpiryDate();
    return expiry != null && expiry.isAfter(LocalDateTime.now());
  }
}
