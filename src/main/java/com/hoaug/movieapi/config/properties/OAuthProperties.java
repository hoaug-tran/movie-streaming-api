package com.hoaug.movieapi.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "oauth")
public class OAuthProperties {
  private Google google = new Google();

  public Google getGoogle () {
    return google;
  }

  public void setGoogle (Google google) {
    this.google = google;
  }

  public static class Google {
    private String clientId;
    private String clientSecret;
    private String redirectUri;
    private String scopes = "openid email profile";

    public String getClientId () {
      return clientId;
    }

    public void setClientId (String clientId) {
      this.clientId = clientId;
    }

    public String getClientSecret () {
      return clientSecret;
    }

    public void setClientSecret (String clientSecret) {
      this.clientSecret = clientSecret;
    }

    public String getRedirectUri () {
      return redirectUri;
    }

    public void setRedirectUri (String redirectUri) {
      this.redirectUri = redirectUri;
    }

    public String getScopes () {
      return scopes;
    }

    public void setScopes (String scopes) {
      this.scopes = scopes;
    }
  }
}
