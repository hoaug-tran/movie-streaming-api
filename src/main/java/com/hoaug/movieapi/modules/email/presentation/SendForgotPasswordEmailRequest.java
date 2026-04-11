package com.hoaug.movieapi.modules.email.presentation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendForgotPasswordEmailRequest {
    private String email;
    private String fullName;
    private String resetLink;
}

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class SendEmailVerificationRequest {
    private String email;
    private String fullName;
    private String verificationLink;
}

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class SendNewMovieNotificationRequest {
    private String email;
    private String fullName;
    private String movieTitle;
    private String moviePosterUrl;
    private String movieLink;
}
