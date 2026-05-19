package com.hoaug.movieapi.modules.support.application;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.hoaug.movieapi.modules.support.domain.ContactMessageRequest;
import com.hoaug.movieapi.modules.support.domain.ContactMessageResponse;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ContactMessageService {

  private static final Map<String, String> TOPIC_LABELS = Map.of(
      "account", "Tài khoản & đăng nhập",
      "billing", "Thanh toán & gói cước",
      "playback", "Trình phát & chất lượng",
      "bug", "Báo lỗi kỹ thuật",
      "partnership", "Hợp tác nội dung",
      "other", "Khác");

  private static final SecureRandom RNG = new SecureRandom();
  private static final DateTimeFormatter TICKET_DATE_FMT = DateTimeFormatter
      .ofPattern("yyMMdd")
      .withZone(ZoneId.of("Asia/Ho_Chi_Minh"));
  private static final DateTimeFormatter HUMAN_FMT = DateTimeFormatter
      .ofPattern("HH:mm dd/MM/yyyy", Locale.forLanguageTag("vi-VN"))
      .withZone(ZoneId.of("Asia/Ho_Chi_Minh"));

  private static final long RATE_LIMIT_PER_HOUR = 5L;
  private static final long RATE_LIMIT_TTL_SECONDS = 60L * 60L;

  private final JavaMailSender mailSender;
  private final RedisTemplate<String, Object> redisTemplate;
  private final String fromEmail;
  private final String fromName;
  private final String supportInbox;
  private final String supportInboxSecondary;

  public ContactMessageService(
      JavaMailSender mailSender,
      RedisTemplate<String, Object> redisTemplate,
      @Value("${mail.from.email:noreply@giophim.libsys.me}") String fromEmail,
      @Value("${mail.from.name:Gió Phim}") String fromName,
      @Value("${app.support.inbox.primary:hi@trkhoang.com}") String supportInbox,
      @Value("${app.support.inbox.secondary:hoaug@duck.com}") String supportInboxSecondary) {
    this.mailSender = mailSender;
    this.redisTemplate = redisTemplate;
    this.fromEmail = fromEmail;
    this.fromName = fromName;
    this.supportInbox = supportInbox;
    this.supportInboxSecondary = supportInboxSecondary;
  }

  public ContactMessageResponse submit(ContactMessageRequest request, String clientIp)
      throws MessagingException, UnsupportedEncodingException {
    enforceRateLimit(clientIp);

    Instant now = Instant.now();
    String ticketId = buildTicketId(now);
    String topicLabel = TOPIC_LABELS.getOrDefault(request.getTopic(), request.getTopic());

    sendEmail(request, ticketId, topicLabel, clientIp, now);

    log.info("Contact ticket {} from {} <{}> topic={} ip={}",
        ticketId, request.getName(), request.getEmail(), request.getTopic(), clientIp);

    return new ContactMessageResponse(ticketId, now);
  }

  private void enforceRateLimit(String clientIp) {
    if (clientIp == null || clientIp.isBlank() || redisTemplate == null) return;
    String key = "support:contact:rate:" + clientIp;
    Long count = redisTemplate.opsForValue().increment(key);
    if (count != null && count == 1L) {
      redisTemplate.expire(key, RATE_LIMIT_TTL_SECONDS, TimeUnit.SECONDS);
    }
    if (count != null && count > RATE_LIMIT_PER_HOUR) {
      throw new TooManyContactRequestsException(
          "Bạn đã gửi quá " + RATE_LIMIT_PER_HOUR + " yêu cầu trong 1 giờ. Vui lòng thử lại sau.");
    }
  }

  private String buildTicketId(Instant now) {
    String datePart = TICKET_DATE_FMT.format(now);
    int random = 1000 + RNG.nextInt(9000);
    return "GP-" + datePart + "-" + random;
  }

  private void sendEmail(ContactMessageRequest request, String ticketId, String topicLabel,
      String clientIp, Instant submittedAt)
      throws MessagingException, UnsupportedEncodingException {
    MimeMessage message = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
    helper.setFrom(fromEmail, fromName);
    helper.setTo(supportInbox);
    if (supportInboxSecondary != null && !supportInboxSecondary.isBlank()
        && !supportInboxSecondary.equalsIgnoreCase(supportInbox)) {
      helper.setCc(supportInboxSecondary);
    }
    helper.setReplyTo(new InternetAddress(request.getEmail(), request.getName()));
    helper.setSubject("[" + ticketId + "] " + topicLabel + " · " + truncate(request.getSubject(), 80));
    helper.setText(buildHtmlBody(request, ticketId, topicLabel, clientIp, submittedAt), true);

    mailSender.send(message);
  }

  private String buildHtmlBody(ContactMessageRequest request, String ticketId, String topicLabel,
      String clientIp, Instant submittedAt) {
    String submittedAtVi = HUMAN_FMT.format(submittedAt);
    String safeMessage = escapeHtml(request.getMessage()).replace("\n", "<br>");
    String safeName = escapeHtml(request.getName());
    String safeEmail = escapeHtml(request.getEmail());
    String safeSubject = escapeHtml(request.getSubject());
    String safeIp = clientIp == null ? "-" : escapeHtml(clientIp);

    return "<!DOCTYPE html><html lang='vi'><head><meta charset='UTF-8'></head>"
        + "<body style='font-family:-apple-system,Segoe UI,Roboto,Helvetica,Arial,sans-serif;background:#f6f7fb;margin:0;padding:24px;color:#1a1a1a'>"
        + "<div style='max-width:640px;margin:0 auto;background:#ffffff;border-radius:12px;overflow:hidden;box-shadow:0 8px 30px rgba(0,0,0,0.06)'>"
        + "<div style='background:#0C0C0C;color:#fff;padding:24px 28px'>"
        + "<div style='font-size:11px;letter-spacing:.18em;color:#C8102E;font-weight:800;text-transform:uppercase'>Gió Phim · Liên hệ</div>"
        + "<div style='font-size:20px;font-weight:800;margin-top:6px'>Yêu cầu hỗ trợ mới</div>"
        + "<div style='font-size:13px;color:#bdbdbd;margin-top:4px'>Mã ticket <strong style='color:#fff'>" + ticketId + "</strong> · Gửi lúc " + submittedAtVi + "</div>"
        + "</div>"
        + "<div style='padding:24px 28px'>"
        + "<table style='width:100%;border-collapse:collapse;font-size:14px'>"
        + row("Người gửi", safeName)
        + row("Email", "<a href='mailto:" + safeEmail + "' style='color:#C8102E;text-decoration:none'>" + safeEmail + "</a>")
        + row("Chủ đề", topicLabel)
        + row("Tiêu đề", "<strong>" + safeSubject + "</strong>")
        + row("IP gửi", safeIp)
        + "</table>"
        + "<div style='margin-top:20px;padding:18px;border-radius:10px;background:#f7f7fb;border-left:4px solid #C8102E;font-size:14px;line-height:1.7;color:#2a2a2a'>"
        + safeMessage
        + "</div>"
        + "<div style='margin-top:20px;font-size:12px;color:#7a7a7a;line-height:1.7'>"
        + "Email tự động từ form Liên hệ tại giophim. Trả lời trực tiếp email này sẽ phản hồi tới người dùng."
        + "</div>"
        + "</div>"
        + "</div></body></html>";
  }

  private String row(String label, String value) {
    return "<tr><td style='padding:6px 0;color:#7a7a7a;width:110px;font-size:12px;letter-spacing:.06em;text-transform:uppercase'>" + label
        + "</td><td style='padding:6px 0;color:#1a1a1a'>" + value + "</td></tr>";
  }

  private String truncate(String s, int max) {
    if (s == null) return "";
    return s.length() <= max ? s : s.substring(0, max - 1) + "…";
  }

  private String escapeHtml(String s) {
    if (s == null) return "";
    return s.replace("&", "&amp;")
        .replace("<", "&lt;")
        .replace(">", "&gt;")
        .replace("\"", "&quot;")
        .replace("'", "&#39;");
  }
}
