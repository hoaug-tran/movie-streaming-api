package com.hoaug.movieapi.modules.notification.infrastructure.persistence.entity;

import com.hoaug.movieapi.common.model.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "push_subscriptions")
public class PushSubscriptionEntity extends BaseEntity {

  @Column(name = "user_id", nullable = false)
  private Long userId;

  @Column(nullable = false, columnDefinition = "TEXT")
  private String endpoint;

  @Column(name = "p256dh", length = 255)
  private String p256dh;

  @Column(name = "auth", length = 255)
  private String auth;

  public Long getUserId () { return userId; }
  public void setUserId (Long userId) { this.userId = userId; }

  public String getEndpoint () { return endpoint; }
  public void setEndpoint (String endpoint) { this.endpoint = endpoint; }

  public String getP256dh () { return p256dh; }
  public void setP256dh (String p256dh) { this.p256dh = p256dh; }

  public String getAuth () { return auth; }
  public void setAuth (String auth) { this.auth = auth; }
}
