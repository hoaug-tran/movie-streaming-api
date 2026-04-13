package com.hoaug.movieapi.modules.payment.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hoaug.movieapi.common.response.ResponseUtil;
import com.hoaug.movieapi.modules.payment.application.service.PaymentService;
import com.hoaug.movieapi.modules.payment.application.service.PaymentService.PaymentLinkResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.payos.PayOS;
import vn.payos.model.webhooks.Webhook;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

  private final PaymentService paymentService;
  private final PayOS payOS;
  private final ObjectMapper objectMapper;

  @PostMapping("/payments/checkout")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<?> createCheckoutLink(@RequestParam Long planId, Authentication auth) {
    try {
      Long userId = Long.parseLong(auth.getName());
      PaymentLinkResponse link = paymentService.createPaymentLink(userId, planId);
      return ResponseUtil.ok(link);
    } catch (Exception e) {
      log.error("Checkout error", e);
      return ResponseUtil.badRequest(e.getMessage());
    }
  }

  @PostMapping("/webhooks/payment")
  public ResponseEntity<String> handlePaymentWebhook(@RequestBody Webhook webhook) {
    try {
      var webhookData = payOS.webhooks().verify(webhook);

      if (webhookData != null && "00".equals(webhookData.getCode())) {
        String orderCode = String.valueOf(webhookData.getOrderCode());
        String transactionId = webhookData.getTransactionDateTime();

        JsonNode fullResponse = objectMapper.readTree(objectMapper.writeValueAsString(webhookData));
        paymentService.completePayment(orderCode, transactionId, fullResponse.toString());

        log.info("Payment webhook processed successfully: orderCode={}", orderCode);
      }

      return ResponseEntity.ok("OK");
    } catch (Exception e) {
      log.error("Webhook error processing payment notification", e);
      return ResponseEntity.status(400).body("ERROR");
    }
  }
}
