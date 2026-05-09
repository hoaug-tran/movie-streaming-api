package com.hoaug.movieapi.modules.auth.application.service;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.UUID;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.auth.application.dto.response.OtpChallengeResponse;
import com.hoaug.movieapi.modules.auth.domain.model.AuthOtpChallenge;
import com.hoaug.movieapi.modules.auth.domain.model.AuthOtpPurpose;
import com.hoaug.movieapi.modules.auth.domain.repository.AuthOtpChallengeRepository;
import com.hoaug.movieapi.modules.email.application.EmailService;

import jakarta.mail.MessagingException;

@Service
public class AuthOtpService {
  private static final int OTP_BOUND = 1_000_000;
  private static final int MAX_ATTEMPTS = 5;
  private static final String RESEND_KEY_PREFIX = "auth:otp:resend:";

  private final AuthOtpChallengeRepository authOtpChallengeRepository;
  private final PasswordEncoder passwordEncoder;
  private final EmailService emailService;
  private final RedisTemplate<String, Object> redisTemplate;
  private final SecureRandom secureRandom = new SecureRandom();

  @Value("${auth.otp.ttl-minutes:10}")
  private long ttlMinutes;

  @Value("${auth.otp.resend-cooldown-seconds:60}")
  private long resendCooldownSeconds;

  public AuthOtpService(AuthOtpChallengeRepository authOtpChallengeRepository,
      PasswordEncoder passwordEncoder, EmailService emailService,
      RedisTemplate<String, Object> redisTemplate) {
    this.authOtpChallengeRepository = authOtpChallengeRepository;
    this.passwordEncoder = passwordEncoder;
    this.emailService = emailService;
    this.redisTemplate = redisTemplate;
  }

  public OtpChallengeResponse issue (AuthOtpPurpose purpose, Long userId, String email,
      String fullName, String payload) {
    String cooldownKey = resendKey(purpose, email);
    Long cooldownTtl = redisTemplate.getExpire(cooldownKey);
    if (cooldownTtl != null && cooldownTtl > 0) {
      throw new AppException(ErrorCode.OTP_RESEND_COOLDOWN);
    }

    String otp = String.format(Locale.ROOT, "%06d", secureRandom.nextInt(OTP_BOUND));
    String challengeToken = UUID.randomUUID().toString().replace("-", "")
        + UUID.randomUUID().toString().replace("-", "");
    LocalDateTime now = LocalDateTime.now();
    Duration ttl = Duration.ofMinutes(ttlMinutes);

    AuthOtpChallenge challenge = new AuthOtpChallenge();
    challenge.setPurpose(purpose.name());
    challenge.setUserId(userId);
    challenge.setEmail(email);
    challenge.setChallengeToken(challengeToken);
    challenge.setOtpHash(passwordEncoder.encode(otp));
    challenge.setPayload(payload);
    challenge.setAttempts(0);
    challenge.setCreatedAt(now);
    challenge.setExpiresAt(now.plus(ttl));
    authOtpChallengeRepository.save(challenge, ttl);

    sendOtpEmail(email, fullName, purpose, otp, ttlMinutes);
    redisTemplate.opsForValue().set(cooldownKey, "1", Duration.ofSeconds(resendCooldownSeconds));

    OtpChallengeResponse response = new OtpChallengeResponse();
    response.setOtpRequired(true);
    response.setChallengeToken(challengeToken);
    response.setEmail(maskEmail(email));
    response.setExpiresInSeconds(ttl.toSeconds());
    response.setResendAfterSeconds(resendCooldownSeconds);
    response.setMessage("Mã OTP đã được gửi đến email của bạn");
    return response;
  }

  public AuthOtpChallenge verify (AuthOtpPurpose purpose, String challengeToken, String otp) {
    AuthOtpChallenge challenge = authOtpChallengeRepository
        .findByPurposeAndChallengeToken(purpose.name(), challengeToken)
        .orElseThrow( () -> new AppException(ErrorCode.OTP_EXPIRED));

    if (challenge.getExpiresAt() == null
        || challenge.getExpiresAt().isBefore(LocalDateTime.now())) {
      authOtpChallengeRepository.deleteByPurposeAndChallengeToken(purpose.name(), challengeToken);
      throw new AppException(ErrorCode.OTP_EXPIRED);
    }

    int attempts = challenge.getAttempts() == null ? 0 : challenge.getAttempts();
    if (attempts >= MAX_ATTEMPTS) {
      authOtpChallengeRepository.deleteByPurposeAndChallengeToken(purpose.name(), challengeToken);
      throw new AppException(ErrorCode.INVALID_OTP);
    }

    if (!passwordEncoder.matches(otp, challenge.getOtpHash())) {
      challenge.setAttempts(attempts + 1);
      Duration remaining = Duration.between(LocalDateTime.now(), challenge.getExpiresAt());
      if (!remaining.isNegative() && !remaining.isZero()) {
        authOtpChallengeRepository.save(challenge, remaining);
      }
      throw new AppException(ErrorCode.INVALID_OTP);
    }

    authOtpChallengeRepository.deleteByPurposeAndChallengeToken(purpose.name(), challengeToken);
    return challenge;
  }

  private void sendOtpEmail (String email, String fullName, AuthOtpPurpose purpose, String otp,
      long ttl) {
    String subject = switch (purpose) {
    case LOGIN -> "Mã OTP đăng nhập Gió phim";
    case REGISTER -> "Mã OTP đăng ký Gió Phim";
    case PASSWORD_RESET -> "Mã OTP đặt lại mật khẩu Gió Phim";
    case PASSWORD_CHANGE -> "Mã OTP đổi mật khẩu Gió Phim";
    };
    String message = "Mã OTP của bạn là " + otp + ". Mã có hiệu lực trong " + ttl
        + " phút. Không chia sẻ mã này với bất kỳ ai.";
    try {
      emailService.sendAccountNotificationEmail(email, fullName, message);
    } catch (MessagingException | UnsupportedEncodingException e) {
      LoggerFactory.getLogger(getClass()).warn("Failed to send OTP email", e);
      throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
    }
  }

  private String maskEmail (String email) {
    int at = email.indexOf('@');
    if (at <= 1) {
      return email;
    }
    return email.charAt(0) + "***" + email.substring(at);
  }

  private String resendKey (AuthOtpPurpose purpose, String email) {
    return RESEND_KEY_PREFIX + purpose.name() + ":" + email.trim().toLowerCase(Locale.ROOT);
  }
}
