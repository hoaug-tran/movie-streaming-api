package com.hoaug.movieapi.common.security;

import org.springframework.stereotype.Component;

@Component
public class SpamDetector {

  public boolean isSpamReview (String content, Integer rating) {
    if (content == null || content.trim().length() < 10) {
      return true;
    }
    if (rating == null || rating < 1 || rating > 10) {
      return true;
    }
    if (hasRepeatingCharacters(content)) {
      return true;
    }
    return false;
  }

  public boolean isSpamComment (String content) {
    if (content == null || content.trim().length() < 3) {
      return true;
    }
    if (hasRepeatingCharacters(content)) {
      return true;
    }
    if (containsExcessiveLinks(content)) {
      return true;
    }
    return false;
  }

  private boolean hasRepeatingCharacters (String content) {
    for (int i = 0; i < content.length() - 4; i++) {
      char c = content.charAt(i);
      if (content.charAt(i + 1) == c && content.charAt(i + 2) == c && content.charAt(i + 3) == c) {
        return true;
      }
    }
    return false;
  }

  private boolean containsExcessiveLinks (String content) {
    return content.toLowerCase().split("http").length > 3;
  }
}
