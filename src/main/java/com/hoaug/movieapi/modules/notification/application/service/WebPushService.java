package com.hoaug.movieapi.modules.notification.application.service;

import java.security.Security;
import java.util.List;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hoaug.movieapi.modules.notification.infrastructure.persistence.entity.PushSubscriptionEntity;
import com.hoaug.movieapi.modules.notification.infrastructure.persistence.repository.JpaPushSubscriptionRepository;

import nl.martijndwars.webpush.Notification;
import nl.martijndwars.webpush.PushService;
import tools.jackson.databind.ObjectMapper;

@Service
public class WebPushService {

  private static final Logger log = LoggerFactory.getLogger(WebPushService.class);

  private final JpaPushSubscriptionRepository pushSubscriptionRepository;
  private final PushService pushService;
  private final ObjectMapper objectMapper;

  public WebPushService(JpaPushSubscriptionRepository pushSubscriptionRepository,
      ObjectMapper objectMapper, @Value("${vapid.public-key}") String vapidPublicKey,
      @Value("${vapid.private-key}") String vapidPrivateKey,
      @Value("${vapid.subject}") String vapidSubject) throws Exception {
    Security.addProvider(new BouncyCastleProvider());
    this.pushSubscriptionRepository = pushSubscriptionRepository;
    this.objectMapper = objectMapper;
    this.pushService = new PushService(vapidPublicKey, vapidPrivateKey, vapidSubject);
  }

  public record PushPayload(String title, String body, String url, String icon) {
  }

  public void sendToUser (Long userId, String title, String body, String actionUrl) {
    List<PushSubscriptionEntity> subscriptions = pushSubscriptionRepository.findByUserId(userId);
    log.info("[WebPush] sendToUser userId={} subscriptions={}", userId, subscriptions.size());
    if (subscriptions.isEmpty()) {
      log.warn("[WebPush] No push subscriptions found for userId={} — push skipped", userId);
      return;
    }

    PushPayload payload = new PushPayload(title, body, actionUrl != null ? actionUrl : "/",
        "/icons/logo.webp");

    String payloadJson;
    try {
      payloadJson = objectMapper.writeValueAsString(payload);
      log.info("[WebPush] payload JSON: {}", payloadJson);
    } catch (Exception e) {
      log.error("[WebPush] Failed to serialize push payload: {}", e.getMessage(), e);
      return;
    }

    java.util.Set<String> seenEndpoints = new java.util.HashSet<>();
    for (PushSubscriptionEntity sub : subscriptions) {
      if (seenEndpoints.add(sub.getEndpoint())) {
        sendToSubscription(sub, payloadJson);
      }
    }
  }

  public void sendToAll (String title, String body, String actionUrl) {
    List<PushSubscriptionEntity> subscriptions = pushSubscriptionRepository.findAll();
    log.info("[WebPush] sendToAll subscriptions={}", subscriptions.size());
    if (subscriptions.isEmpty()) {
      log.warn("[WebPush] No push subscriptions in DB — broadcast push skipped");
      return;
    }

    PushPayload payload = new PushPayload(title, body, actionUrl != null ? actionUrl : "/",
        "/icons/logo.webp");

    String payloadJson;
    try {
      payloadJson = objectMapper.writeValueAsString(payload);
      log.info("[WebPush] broadcast payload JSON: {}", payloadJson);
    } catch (Exception e) {
      log.error("[WebPush] Failed to serialize broadcast payload: {}", e.getMessage(), e);
      return;
    }

    
    java.util.Set<String> seenEndpoints = new java.util.HashSet<>();
    for (PushSubscriptionEntity sub : subscriptions) {
      if (seenEndpoints.add(sub.getEndpoint())) {
        sendToSubscription(sub, payloadJson);
      }
    }
  }

  private void sendToSubscription (PushSubscriptionEntity sub, String payloadJson) {
    log.info("[WebPush] Sending to userId={} endpoint={}", sub.getUserId(),
        sub.getEndpoint().length() > 60 ? sub.getEndpoint().substring(0, 60) + "..."
            : sub.getEndpoint());
    try {
      Notification notification = new Notification(sub.getEndpoint(), sub.getP256dh(),
          sub.getAuth(), payloadJson.getBytes());
      pushService.send(notification);
      log.info("[WebPush] Push sent OK to userId={}", sub.getUserId());
    } catch (Exception e) {
      log.error("[WebPush] FAILED to send push to userId={} — {}: {}", sub.getUserId(),
          e.getClass().getSimpleName(), e.getMessage(), e);
      if (e.getMessage() != null
          && (e.getMessage().contains("410") || e.getMessage().contains("404"))) {
        pushSubscriptionRepository.delete(sub);
        log.info("[WebPush] Removed expired subscription for userId={}", sub.getUserId());
      }
    }
  }
}
