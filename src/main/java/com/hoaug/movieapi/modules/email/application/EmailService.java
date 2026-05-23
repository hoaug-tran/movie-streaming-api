package com.hoaug.movieapi.modules.email.application;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.hoaug.movieapi.modules.email.domain.EmailRequest;
import com.hoaug.movieapi.modules.email.domain.EmailType;
import com.hoaug.movieapi.modules.email.infrastructure.EmailTemplateProvider;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

  private final JavaMailSender mailSender;
  private final EmailTemplateProvider emailTemplateProvider;

  @Value("${mail.from.email:noreply@giophim.libsys.me}")
  private String fromEmail;

  @Value("${mail.from.name:Gio Phim}")
  private String fromName;

  private void sendHtmlEmail (String to, String subject, String htmlContent)
      throws MessagingException, UnsupportedEncodingException {
    MimeMessage message = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

    helper.setFrom(fromEmail, fromName);
    helper.setTo(to);
    helper.setSubject(subject);
    helper.setText(htmlContent, true);

    mailSender.send(message);
  }

  public boolean sendForgotPasswordEmail (String to, String resetLink, String fullName)
      throws MessagingException, UnsupportedEncodingException {
    Map<String, String> variables = new HashMap<>();
    variables.put("fullName", fullName);
    variables.put("resetLink", resetLink);

    String htmlContent = emailTemplateProvider.getTemplate(EmailType.FORGOT_PASSWORD, variables);
    sendHtmlEmail(to, "Yêu cầu đặt lại mật khẩu của bạn", htmlContent);
    return true;
  }

  public boolean sendResetPasswordSuccessEmail (String to, String fullName)
      throws MessagingException, UnsupportedEncodingException {
    Map<String, String> variables = new HashMap<>();
    variables.put("fullName", fullName);

    String htmlContent = emailTemplateProvider.getTemplate(EmailType.RESET_PASSWORD, variables);
    sendHtmlEmail(to, "Mật khẩu của bạn đã được đặt lại thành công", htmlContent);
    return true;
  }

  public boolean sendSignupSuccessEmail (String to, String fullName, String verificationLink)
      throws MessagingException, UnsupportedEncodingException {
    Map<String, String> variables = new HashMap<>();
    variables.put("fullName", fullName);
    variables.put("verificationLink", verificationLink);

    String htmlContent = emailTemplateProvider.getTemplate(EmailType.SIGNUP_SUCCESS, variables);
    sendHtmlEmail(to, "Chào mừng bạn đến với Gió Phim", htmlContent);
    return true;
  }

  public boolean sendEmailVerificationEmail (String to, String fullName, String verificationLink)
      throws MessagingException, UnsupportedEncodingException {
    Map<String, String> variables = new HashMap<>();
    variables.put("fullName", fullName);
    variables.put("verificationLink", verificationLink);

    String htmlContent = emailTemplateProvider.getTemplate(EmailType.EMAIL_VERIFICATION, variables);
    sendHtmlEmail(to, "Vui lòng xác minh địa chỉ email của bạn", htmlContent);
    return true;
  }

  public boolean sendAccountNotificationEmail (String to, String fullName,
      String notificationMessage) throws MessagingException, UnsupportedEncodingException {
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
      throws MessagingException, UnsupportedEncodingException {
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
      throws MessagingException, UnsupportedEncodingException {
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
      throws MessagingException, UnsupportedEncodingException {
    String htmlContent = emailTemplateProvider.getTemplate(emailRequest.getEmailType(),
        emailRequest.getTemplateVariables());
    sendHtmlEmail(emailRequest.getTo(), emailRequest.getSubject(), htmlContent);
    return true;
  }
}
