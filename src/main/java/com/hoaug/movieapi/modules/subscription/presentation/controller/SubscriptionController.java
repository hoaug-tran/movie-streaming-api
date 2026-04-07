package com.hoaug.movieapi.modules.subscription.presentation.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.auth.domain.repository.AuthUserRepository;
import com.hoaug.movieapi.modules.subscription.application.dto.request.CreateInvoiceRequest;
import com.hoaug.movieapi.modules.subscription.application.dto.request.CreatePaymentTransactionRequest;
import com.hoaug.movieapi.modules.subscription.application.dto.request.CreateSubscriptionPlanRequest;
import com.hoaug.movieapi.modules.subscription.application.dto.request.SubscribePlanRequest;
import com.hoaug.movieapi.modules.subscription.application.dto.response.InvoiceResponse;
import com.hoaug.movieapi.modules.subscription.application.dto.response.PaymentTransactionResponse;
import com.hoaug.movieapi.modules.subscription.application.dto.response.SubscriptionPlanResponse;
import com.hoaug.movieapi.modules.subscription.application.dto.response.UserSubscriptionResponse;
import com.hoaug.movieapi.modules.subscription.application.usecase.CreateInvoiceUseCase;
import com.hoaug.movieapi.modules.subscription.application.usecase.CreatePaymentTransactionUseCase;
import com.hoaug.movieapi.modules.subscription.application.usecase.CreateSubscriptionPlanUseCase;
import com.hoaug.movieapi.modules.subscription.application.usecase.GetActiveSubscriptionPlansUseCase;
import com.hoaug.movieapi.modules.subscription.application.usecase.GetMyInvoicesUseCase;
import com.hoaug.movieapi.modules.subscription.application.usecase.GetMyPaymentTransactionsUseCase;
import com.hoaug.movieapi.modules.subscription.application.usecase.GetMySubscriptionsUseCase;
import com.hoaug.movieapi.modules.subscription.application.usecase.MarkPaymentSuccessUseCase;
import com.hoaug.movieapi.modules.subscription.application.usecase.SubscribePlanUseCase;
import com.hoaug.movieapi.modules.user.domain.model.User;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

  private final CreateSubscriptionPlanUseCase createSubscriptionPlanUseCase;
  private final GetActiveSubscriptionPlansUseCase getActiveSubscriptionPlansUseCase;
  private final SubscribePlanUseCase subscribePlanUseCase;
  private final GetMySubscriptionsUseCase getMySubscriptionsUseCase;
  private final CreatePaymentTransactionUseCase createPaymentTransactionUseCase;
  private final MarkPaymentSuccessUseCase markPaymentSuccessUseCase;
  private final GetMyPaymentTransactionsUseCase getMyPaymentTransactionsUseCase;
  private final CreateInvoiceUseCase createInvoiceUseCase;
  private final GetMyInvoicesUseCase getMyInvoicesUseCase;
  private final AuthUserRepository authUserRepository;

  public SubscriptionController(CreateSubscriptionPlanUseCase createSubscriptionPlanUseCase,
      GetActiveSubscriptionPlansUseCase getActiveSubscriptionPlansUseCase,
      SubscribePlanUseCase subscribePlanUseCase,
      GetMySubscriptionsUseCase getMySubscriptionsUseCase,
      CreatePaymentTransactionUseCase createPaymentTransactionUseCase,
      MarkPaymentSuccessUseCase markPaymentSuccessUseCase,
      GetMyPaymentTransactionsUseCase getMyPaymentTransactionsUseCase,
      CreateInvoiceUseCase createInvoiceUseCase, GetMyInvoicesUseCase getMyInvoicesUseCase,
      AuthUserRepository authUserRepository) {
    this.createSubscriptionPlanUseCase = createSubscriptionPlanUseCase;
    this.getActiveSubscriptionPlansUseCase = getActiveSubscriptionPlansUseCase;
    this.subscribePlanUseCase = subscribePlanUseCase;
    this.getMySubscriptionsUseCase = getMySubscriptionsUseCase;
    this.createPaymentTransactionUseCase = createPaymentTransactionUseCase;
    this.markPaymentSuccessUseCase = markPaymentSuccessUseCase;
    this.getMyPaymentTransactionsUseCase = getMyPaymentTransactionsUseCase;
    this.createInvoiceUseCase = createInvoiceUseCase;
    this.getMyInvoicesUseCase = getMyInvoicesUseCase;
    this.authUserRepository = authUserRepository;
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping("/plans")
  public SubscriptionPlanResponse createPlan (
      @Valid @RequestBody CreateSubscriptionPlanRequest request) {
    return createSubscriptionPlanUseCase.execute(request);
  }

  @GetMapping("/plans")
  public List<SubscriptionPlanResponse> getActivePlans () {
    return getActiveSubscriptionPlansUseCase.execute();
  }

  @PostMapping("/subscribe")
  public UserSubscriptionResponse subscribe (Authentication authentication,
      @Valid @RequestBody SubscribePlanRequest request) {
    return subscribePlanUseCase.execute(getCurrentUserId(authentication), request);
  }

  @GetMapping("/me")
  public List<UserSubscriptionResponse> getMySubscriptions (Authentication authentication) {
    return getMySubscriptionsUseCase.execute(getCurrentUserId(authentication));
  }

  @PostMapping("/payments")
  public PaymentTransactionResponse createPayment (Authentication authentication,
      @Valid @RequestBody CreatePaymentTransactionRequest request) {
    return createPaymentTransactionUseCase.execute(getCurrentUserId(authentication), request);
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PatchMapping("/payments/{transactionId}/success")
  public PaymentTransactionResponse markPaymentSuccess (@PathVariable Long transactionId,
      @RequestParam String providerTransactionId,
      @RequestParam(required = false) String providerResponse) {
    return markPaymentSuccessUseCase.execute(transactionId, providerTransactionId,
        providerResponse);
  }

  @GetMapping("/payments/me")
  public List<PaymentTransactionResponse> getMyPayments (Authentication authentication) {
    return getMyPaymentTransactionsUseCase.execute(getCurrentUserId(authentication));
  }

  @PostMapping("/invoices")
  public InvoiceResponse createInvoice (@Valid @RequestBody CreateInvoiceRequest request) {
    return createInvoiceUseCase.execute(request);
  }

  @GetMapping("/invoices/me")
  public List<InvoiceResponse> getMyInvoices (Authentication authentication) {
    return getMyInvoicesUseCase.execute(getCurrentUserId(authentication));
  }

  private Long getCurrentUserId (Authentication authentication) {
    User user = authUserRepository.findByUsername(authentication.getName())
        .orElseThrow( () -> new AppException(ErrorCode.USER_NOT_FOUND));
    return user.getId();
  }
}