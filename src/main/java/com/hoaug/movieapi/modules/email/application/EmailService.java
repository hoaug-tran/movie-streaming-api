package com.hoaug.movieapi.modules.email.application;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hoaug.movieapi.modules.email.domain.EmailRequest;
import com.hoaug.movieapi.modules.email.domain.EmailType;
import com.hoaug.movieapi.modules.email.infrastructure.EmailTemplateProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

  private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

  private final EmailTemplateProvider emailTemplateProvider;
  private final ObjectMapper objectMapper;

  private final OkHttpClient httpClient = new OkHttpClient();

  @Value("${mail.from.email:noreply@giophim.libsys.me}")
  private String fromEmail;

  @Value("${mail.from.name:Gio Phim}")
  private String fromName;

  @Value("${mailtrap.api-token:}")
  private String mailtrapApiToken;

  @Value("${mailtrap.api-url:https://send.api.mailtrap.io/api/send}")
  private String mailtrapApiUrl;

  private void sendHtmlEmail (String to, String subject, String htmlContent) throws IOException {
    if (mailtrapApiToken == null || mailtrapApiToken.isBlank()) {
      throw new IOException("MAILTRAP_API_TOKEN is not configured");
    }

    Map<String, Object> payload = Map.of(
        "from", Map.of("email", fromEmail, "name", fromName),
        "to", java.util.List.of(Map.of("email", to)),
        "subject", subject,
        "html", htmlContent,
        "category", "Gio Phim Transactional"
    );

    Request request = new Request.Builder()
        .url(mailtrapApiUrl)
        .header("Authorization", "Bearer " + mailtrapApiToken)
        .header("Content-Type", "application/json")
        .post(RequestBody.create(objectMapper.writeValueAsString(payload), JSON))
        .build();

    try (Response response = httpClient.newCall(request).execute()) {
      if (!response.isSuccessful()) {
        String body = response.body() != null ? response.body().string() : "";
        log.error("Mailtrap API returned {} - body: {}", response.code(), body);
        throw new IOException("Mailtrap API returned status " + response.code());
      }
    }
  }

  public boolean sendForgotPasswordEmail (String to, String resetLink, String fullName)
      throws IOException {
    Map<String, String> variables = new HashMap<>();
    variables.put("fullName", fullName);
    variables.put("resetLink", resetLink);

    String htmlContent = emailTemplateProvider.getTemplate(EmailType.FORGOT_PASSWORD, variables);
    sendHtmlEmail(to, "Yêu cầu đặt lại mật khẩu của bạn", htmlContent);
    return true;
  }

  public boolean sendResetPasswordSuccessEmail (String to, String fullName)
      throws IOException {
    Map<String, String> variables = new HashMap<>();
    variables.put("fullName", fullName);

    String htmlContent = emailTemplateProvider.getTemplate(EmailType.RESET_PASSWORD, variables);
    sendHtmlEmail(to, "Mật khẩu của bạn đã được đặt lại thành công", htmlContent);
    return true;
  }

  public boolean sendSignupSuccessEmail (String to, String fullName, String verificationLink)
      throws IOException {
    Map<String, String> variables = new HashMap<>();
    variables.put("fullName", fullName);
    variables.put("verificationLink", verificationLink);

    String htmlContent = emailTemplateProvider.getTemplate(EmailType.SIGNUP_SUCCESS, variables);
    sendHtmlEmail(to, "Chào mừng bạn đến với Gió Phim", htmlContent);
    return true;
  }

  public boolean sendEmailVerificationEmail (String to, String fullName, String verificationLink)
      throws IOException {
    Map<String, String> variables = new HashMap<>();
    variables.put("fullName", fullName);
    variables.put("verificationLink", verificationLink);

    String htmlContent = emailTemplateProvider.getTemplate(EmailType.EMAIL_VERIFICATION, variables);
    sendHtmlEmail(to, "Vui lòng xác minh địa chỉ email của bạn", htmlContent);
    return true;
  }

  public boolean sendAccountNotificationEmail (String to, String fullName,
      String notificationMessage) throws IOException {
    Map<String, String> variables = new HashMap<>();
    variables.put("fullName", fullName);
    variables.put("notificationMessage", notificationMessage);

    String htmlContent = emailTemplateProvider.getTemplate(EmailType.ACCOUNT_NOTIFICATION,
        variables);
    sendHtmlEmail(to, "Thông báo từ Gió Phim", htmlContent);
    return true;
  }

  public boolean sendNewMovieReleaseEmail (String to, String fullName, String movieTitle,
      String moviePosterUrl, String movieLink)
      throws IOException {
    Map<String, String> variables = new HashMap<>();
    variables.put("fullName", fullName);
    variables.put("movieTitle", movieTitle);
    variables.put("moviePosterUrl", moviePosterUrl);
    variables.put("movieLink", movieLink);

    String htmlContent = emailTemplateProvider.getTemplate(EmailType.NEW_MOVIE_RELEASE, variables);
    sendHtmlEmail(to, "🎬 " + movieTitle + " đã phát hành", htmlContent);
    return true;
  }

  public boolean sendOtpVerificationEmail (String to, String fullName, String otp,
      String purposeLabel, long ttlMinutes)
      throws IOException {
    Map<String, String> variables = new HashMap<>();
    variables.put("fullName", fullName);
    variables.put("otp", otp);
    variables.put("purposeLabel", purposeLabel);
    variables.put("ttlMinutes", String.valueOf(ttlMinutes));

    String htmlContent = emailTemplateProvider.getTemplate(EmailType.OTP_VERIFICATION, variables);
    sendHtmlEmail(to, "Mã xác thực Gió Phim: " + otp, htmlContent);
    return true;
  }

  public boolean sendCustomEmail (EmailRequest emailRequest)
      throws IOException {
    String htmlContent = emailTemplateProvider.getTemplate(emailRequest.getEmailType(),
        emailRequest.getTemplateVariables());
    sendHtmlEmail(emailRequest.getTo(), emailRequest.getSubject(), htmlContent);
    return true;
  }
}
