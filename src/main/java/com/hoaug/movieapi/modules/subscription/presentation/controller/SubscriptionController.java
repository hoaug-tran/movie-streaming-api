package com.hoaug.movieapi.modules.subscription.presentation.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.common.response.ResponseUtil;
import com.hoaug.movieapi.modules.auth.domain.repository.AuthUserRepository;
import com.hoaug.movieapi.modules.subscription.application.dto.request.CreateInvoiceRequest;
import com.hoaug.movieapi.modules.subscription.application.dto.request.CreatePaymentTransactionRequest;
import com.hoaug.movieapi.modules.subscription.application.dto.request.CreateSubscriptionPlanRequest;
import com.hoaug.movieapi.modules.subscription.application.dto.request.SubscribePlanRequest;
import com.hoaug.movieapi.modules.subscription.application.dto.request.UpdateAutoRenewRequest;
import com.hoaug.movieapi.modules.subscription.application.dto.request.UpdateSubscriptionPlanRequest;
import com.hoaug.movieapi.modules.subscription.application.dto.response.InvoiceResponse;
import com.hoaug.movieapi.modules.subscription.application.dto.response.PaymentTransactionResponse;
import com.hoaug.movieapi.modules.subscription.application.dto.response.SubscriptionPlanResponse;
import com.hoaug.movieapi.modules.subscription.application.dto.response.UserSubscriptionResponse;
import com.hoaug.movieapi.modules.subscription.application.usecase.CreateInvoiceUseCase;
import com.hoaug.movieapi.modules.subscription.application.usecase.CreatePaymentTransactionUseCase;
import com.hoaug.movieapi.modules.subscription.application.usecase.CreateSubscriptionPlanUseCase;
import com.hoaug.movieapi.modules.subscription.application.usecase.GetActiveSubscriptionPlansUseCase;
import com.hoaug.movieapi.modules.subscription.application.usecase.GetMyCurrentSubscriptionUseCase;
import com.hoaug.movieapi.modules.subscription.application.usecase.GetMyInvoicesUseCase;
import com.hoaug.movieapi.modules.subscription.application.usecase.GetMyPaymentTransactionsUseCase;
import com.hoaug.movieapi.modules.subscription.application.usecase.GetMySubscriptionsUseCase;
import com.hoaug.movieapi.modules.subscription.application.usecase.MarkPaymentSuccessUseCase;
import com.hoaug.movieapi.modules.subscription.application.usecase.SubscribePlanUseCase;
import com.hoaug.movieapi.modules.subscription.application.usecase.UpdateMyAutoRenewUseCase;
import com.hoaug.movieapi.modules.subscription.application.usecase.UpdateSubscriptionPlanUseCase;
import com.hoaug.movieapi.modules.subscription.application.usecase.DeleteSubscriptionPlanUseCase;
import com.hoaug.movieapi.modules.user.domain.model.User;

import jakarta.validation.Valid;

@RestController
@RequestMapping("${api.prefix:/api/v1}/subscriptions")
public class SubscriptionController {

  private final CreateSubscriptionPlanUseCase createSubscriptionPlanUseCase;
  private final UpdateSubscriptionPlanUseCase updateSubscriptionPlanUseCase;
  private final DeleteSubscriptionPlanUseCase deleteSubscriptionPlanUseCase;
  private final GetActiveSubscriptionPlansUseCase getActiveSubscriptionPlansUseCase;
  private final SubscribePlanUseCase subscribePlanUseCase;
  private final GetMySubscriptionsUseCase getMySubscriptionsUseCase;
  private final GetMyCurrentSubscriptionUseCase getMyCurrentSubscriptionUseCase;
  private final UpdateMyAutoRenewUseCase updateMyAutoRenewUseCase;
  private final CreatePaymentTransactionUseCase createPaymentTransactionUseCase;
  private final MarkPaymentSuccessUseCase markPaymentSuccessUseCase;
  private final GetMyPaymentTransactionsUseCase getMyPaymentTransactionsUseCase;
  private final CreateInvoiceUseCase createInvoiceUseCase;
  private final GetMyInvoicesUseCase getMyInvoicesUseCase;
  private final AuthUserRepository authUserRepository;

  public SubscriptionController(CreateSubscriptionPlanUseCase createSubscriptionPlanUseCase,
      UpdateSubscriptionPlanUseCase updateSubscriptionPlanUseCase,
      DeleteSubscriptionPlanUseCase deleteSubscriptionPlanUseCase,
      GetActiveSubscriptionPlansUseCase getActiveSubscriptionPlansUseCase,
      SubscribePlanUseCase subscribePlanUseCase,
      GetMySubscriptionsUseCase getMySubscriptionsUseCase,
      GetMyCurrentSubscriptionUseCase getMyCurrentSubscriptionUseCase,
      UpdateMyAutoRenewUseCase updateMyAutoRenewUseCase,
      CreatePaymentTransactionUseCase createPaymentTransactionUseCase,
      MarkPaymentSuccessUseCase markPaymentSuccessUseCase,
      GetMyPaymentTransactionsUseCase getMyPaymentTransactionsUseCase,
      CreateInvoiceUseCase createInvoiceUseCase, GetMyInvoicesUseCase getMyInvoicesUseCase,
      AuthUserRepository authUserRepository) {
    this.createSubscriptionPlanUseCase = createSubscriptionPlanUseCase;
    this.updateSubscriptionPlanUseCase = updateSubscriptionPlanUseCase;
    this.deleteSubscriptionPlanUseCase = deleteSubscriptionPlanUseCase;
    this.getActiveSubscriptionPlansUseCase = getActiveSubscriptionPlansUseCase;
    this.subscribePlanUseCase = subscribePlanUseCase;
    this.getMySubscriptionsUseCase = getMySubscriptionsUseCase;
    this.getMyCurrentSubscriptionUseCase = getMyCurrentSubscriptionUseCase;
    this.updateMyAutoRenewUseCase = updateMyAutoRenewUseCase;
    this.createPaymentTransactionUseCase = createPaymentTransactionUseCase;
    this.markPaymentSuccessUseCase = markPaymentSuccessUseCase;
    this.getMyPaymentTransactionsUseCase = getMyPaymentTransactionsUseCase;
    this.createInvoiceUseCase = createInvoiceUseCase;
    this.getMyInvoicesUseCase = getMyInvoicesUseCase;
    this.authUserRepository = authUserRepository;
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping("/plans")
  public ResponseEntity<SubscriptionPlanResponse> createPlan (
      @Valid @RequestBody CreateSubscriptionPlanRequest request) {
    return ResponseUtil.created(createSubscriptionPlanUseCase.execute(request));
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PutMapping("/plans/{planId}")
  public ResponseEntity<SubscriptionPlanResponse> updatePlan (@PathVariable Long planId,
      @Valid @RequestBody UpdateSubscriptionPlanRequest request) {
    return ResponseUtil.ok(updateSubscriptionPlanUseCase.execute(planId, request));
  }

  @PreAuthorize("hasRole('ADMIN')")
  @DeleteMapping("/plans/{planId}")
  public ResponseEntity<Void> deletePlan (@PathVariable Long planId) {
    deleteSubscriptionPlanUseCase.execute(planId);
    return ResponseUtil.noContent();
  }

  @GetMapping("/plans")
  public ResponseEntity<List<SubscriptionPlanResponse>> getActivePlans () {
    return ResponseUtil.ok(getActiveSubscriptionPlansUseCase.execute());
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping("/admin/assign/{userId}")
  public ResponseEntity<UserSubscriptionResponse> adminAssignSubscription(
      @PathVariable Long userId,
      @Valid @RequestBody SubscribePlanRequest request) {
    return ResponseEntity.ok(subscribePlanUseCase.executeForUser(userId, request));
  }

  @PostMapping("/subscribe")
  public ResponseEntity<UserSubscriptionResponse> subscribe (Authentication authentication,
      @Valid @RequestBody SubscribePlanRequest request) {
    return ResponseUtil
        .created(subscribePlanUseCase.execute(getCurrentUserId(authentication), request));
  }

  @GetMapping("/me")
  public ResponseEntity<List<UserSubscriptionResponse>> getMySubscriptions (
      Authentication authentication) {
    return ResponseUtil.ok(getMySubscriptionsUseCase.execute(getCurrentUserId(authentication)).getItems());
  }

  @GetMapping("/me/current")
  public ResponseEntity<UserSubscriptionResponse> getMyCurrentSubscription (
      Authentication authentication) {
    return getMyCurrentSubscriptionUseCase.execute(getCurrentUserId(authentication))
        .map(ResponseUtil::ok).orElseGet( () -> ResponseEntity.noContent().build());
  }

  @GetMapping("/me/history")
  public ResponseEntity<List<UserSubscriptionResponse>> getMySubscriptionHistory (
      Authentication authentication) {
    return ResponseUtil.ok(getMySubscriptionsUseCase.execute(getCurrentUserId(authentication)).getItems());
  }

  @PatchMapping("/me/current/auto-renew")
  public ResponseEntity<UserSubscriptionResponse> updateMyAutoRenew (Authentication authentication,
      @Valid @RequestBody UpdateAutoRenewRequest request) {
    return ResponseUtil
        .ok(updateMyAutoRenewUseCase.execute(getCurrentUserId(authentication), request));
  }

  @PostMapping("/payments")
  public ResponseEntity<PaymentTransactionResponse> createPayment (Authentication authentication,
      @Valid @RequestBody CreatePaymentTransactionRequest request) {
    return ResponseUtil.created(
        createPaymentTransactionUseCase.execute(getCurrentUserId(authentication), request));
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PatchMapping("/payments/{transactionId}/success")
  public ResponseEntity<PaymentTransactionResponse> markPaymentSuccess (
      @PathVariable Long transactionId, @RequestParam String providerTransactionId,
      @RequestParam(required = false) String providerResponse) {
    return ResponseUtil.ok(
        markPaymentSuccessUseCase.execute(transactionId, providerTransactionId, providerResponse));
  }

  @GetMapping("/payments/me")
  public ResponseEntity<List<PaymentTransactionResponse>> getMyPayments (
      Authentication authentication) {
    return ResponseUtil
        .ok(getMyPaymentTransactionsUseCase.execute(getCurrentUserId(authentication)));
  }

  @PostMapping("/invoices")
  public ResponseEntity<InvoiceResponse> createInvoice (
      @Valid @RequestBody CreateInvoiceRequest request) {
    return ResponseUtil.created(createInvoiceUseCase.execute(request));
  }

  @GetMapping("/invoices/me")
  public ResponseEntity<List<InvoiceResponse>> getMyInvoices (Authentication authentication) {
    return ResponseUtil.ok(getMyInvoicesUseCase.execute(getCurrentUserId(authentication)));
  }

  private Long getCurrentUserId (Authentication authentication) {
    User user = authUserRepository.findByUsername(authentication.getName())
        .orElseThrow( () -> new AppException(ErrorCode.USER_NOT_FOUND));
    return user.getId();
  }
}