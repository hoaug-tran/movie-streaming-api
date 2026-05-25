package com.hoaug.movieapi.modules.notification.application.listener;

import java.time.LocalDateTime;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.event.CommentCreatedEvent;
import com.hoaug.movieapi.common.event.CommentLikedEvent;
import com.hoaug.movieapi.common.event.ReviewLikedEvent;
import com.hoaug.movieapi.common.event.SubscriptionActivatedEvent;
import com.hoaug.movieapi.modules.movie.domain.repository.MovieRepository;
import com.hoaug.movieapi.modules.notification.domain.model.Notification;
import com.hoaug.movieapi.modules.notification.domain.model.NotificationType;
import com.hoaug.movieapi.modules.notification.domain.repository.NotificationRepository;
import com.hoaug.movieapi.modules.subscription.domain.repository.SubscriptionPlanRepository;

@Component
public class NotificationEventListener {

  private final NotificationRepository notificationRepository;
  private final SubscriptionPlanRepository subscriptionPlanRepository;
  private final MovieRepository movieRepository;

  public NotificationEventListener(NotificationRepository notificationRepository,
      SubscriptionPlanRepository subscriptionPlanRepository,
      MovieRepository movieRepository) {
    this.notificationRepository = notificationRepository;
    this.subscriptionPlanRepository = subscriptionPlanRepository;
    this.movieRepository = movieRepository;
  }

  @Async
  @EventListener
  public void onSubscriptionActivated(SubscriptionActivatedEvent event) {
    String planName = subscriptionPlanRepository.findById(event.getSubscriptionPlanId())
        .map(plan -> plan.getName())
        .orElse("Premium");

    Notification notification = new Notification();
    notification.setUserId(event.getUserId());
    notification.setTitle("Thanh toán thành công 🎉");
    notification.setContent(
        "Gói " + planName + " đã được kích hoạt thành công. Chúc bạn xem phim vui vẻ!");
    notification.setType(NotificationType.PAYMENT_SUCCESS);
    notification.setIsRead(false);
    notification.setActionUrl("/profile/subscription");
    notification.setCreatedAt(LocalDateTime.now());
    notificationRepository.save(notification);
  }

  @Async
  @EventListener
  public void onCommentCreated(CommentCreatedEvent event) {
    if (event.getParentCommentUserId() == null) return;
    if (event.getParentCommentUserId().equals(event.getAuthorUserId())) return;

    boolean alreadyNotified = notificationRepository.existsByUserIdAndTypeAndCreatedAtAfter(
        event.getParentCommentUserId(),
        NotificationType.COMMENT_REPLY.name(),
        LocalDateTime.now().minusMinutes(30));
    if (alreadyNotified) return;

    String actionUrl = buildMovieCommentUrl(event.getMovieSlug(), event.getParentCommentId());

    Notification notification = new Notification();
    notification.setUserId(event.getParentCommentUserId());
    notification.setTitle("Có người phản hồi bình luận của bạn 💬");
    notification.setContent("Bình luận của bạn vừa nhận được một phản hồi mới.");
    notification.setType(NotificationType.COMMENT_REPLY);
    notification.setIsRead(false);
    notification.setActionUrl(actionUrl);
    notification.setReferenceId(event.getCommentId());
    notification.setCreatedAt(LocalDateTime.now());
    notificationRepository.save(notification);
  }

  @Async
  @EventListener
  public void onCommentLiked(CommentLikedEvent event) {
    if (event.getCommentOwnerId() == null) return;
    if (event.getCommentOwnerId().equals(event.getLikerUserId())) return;

    boolean alreadyNotified = notificationRepository.existsByUserIdAndTypeAndCreatedAtAfter(
        event.getCommentOwnerId(),
        NotificationType.COMMENT_LIKE.name(),
        LocalDateTime.now().minusHours(1));
    if (alreadyNotified) return;

    String movieSlug = event.getMovieSlug() != null ? event.getMovieSlug()
        : movieRepository.findById(event.getMovieId()).map(m -> m.getSlug()).orElse(null);

    String actionUrl = buildMovieCommentUrl(movieSlug, event.getCommentId());

    Notification notification = new Notification();
    notification.setUserId(event.getCommentOwnerId());
    notification.setTitle("Bình luận của bạn được thích ❤️");
    notification.setContent("Có người vừa thích bình luận của bạn.");
    notification.setType(NotificationType.COMMENT_LIKE);
    notification.setIsRead(false);
    notification.setActionUrl(actionUrl);
    notification.setReferenceId(event.getCommentId());
    notification.setCreatedAt(LocalDateTime.now());
    notificationRepository.save(notification);
  }

  @Async
  @EventListener
  public void onReviewLiked(ReviewLikedEvent event) {
    if (event.getReviewOwnerId() == null) return;
    if (event.getReviewOwnerId().equals(event.getLikerUserId())) return;

    boolean alreadyNotified = notificationRepository.existsByUserIdAndTypeAndCreatedAtAfter(
        event.getReviewOwnerId(),
        NotificationType.REVIEW_LIKE.name(),
        LocalDateTime.now().minusHours(1));
    if (alreadyNotified) return;

    String movieSlug = movieRepository.findById(event.getMovieId())
        .map(m -> m.getSlug()).orElse(null);

    String actionUrl = movieSlug != null ? "/movies/" + movieSlug + "#reviews" : null;

    Notification notification = new Notification();
    notification.setUserId(event.getReviewOwnerId());
    notification.setTitle("Đánh giá của bạn được thích ⭐");
    notification.setContent("Có người vừa thích đánh giá phim của bạn.");
    notification.setType(NotificationType.REVIEW_LIKE);
    notification.setIsRead(false);
    notification.setActionUrl(actionUrl);
    notification.setReferenceId(event.getReviewId());
    notification.setCreatedAt(LocalDateTime.now());
    notificationRepository.save(notification);
  }

  
  private String buildMovieCommentUrl(String movieSlug, Long commentId) {
    if (movieSlug == null) return null;
    StringBuilder url = new StringBuilder("/movies/").append(movieSlug);
    if (commentId != null) {
      url.append("?comment=").append(commentId);
    }
    return url.toString();
  }
}
