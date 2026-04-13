package com.hoaug.movieapi.common.util;

public class HtmlSanitizer {

  public static String sanitize (String input) {
    if (input == null) {
      return null;
    }

    return input.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
        .replace("\"", "&quot;").replace("'", "&#x27;");
  }

  public static String sanitizeUrl (String url) {
    if (url == null) {
      return null;
    }

    if (!url.matches("https?://.*")) {
      return null;
    }

    return sanitize(url);
  }

  public static String sanitizeEmail (String email) {
    if (email == null) {
      return null;
    }

    return email.trim().toLowerCase();
  }
}
