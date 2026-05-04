package com.hoaug.movieapi.modules.auth.application.usecase;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.auth.application.dto.response.CurrentUserResponse;
import com.hoaug.movieapi.modules.auth.domain.repository.AuthUserRepository;
import com.hoaug.movieapi.modules.user.domain.model.User;
import com.hoaug.movieapi.shared.media.MediaUrlResolver;

@Component
public class GetCurrentUserUseCase {
    private final AuthUserRepository authUserRepository;
    private final MediaUrlResolver mediaUrlResolver;

    public GetCurrentUserUseCase(AuthUserRepository authUserRepository, MediaUrlResolver mediaUrlResolver) {
        this.authUserRepository = authUserRepository;
        this.mediaUrlResolver = mediaUrlResolver;
    }

    public CurrentUserResponse execute(String username) {
        User user = authUserRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        CurrentUserResponse response = new CurrentUserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setFullName(user.getFullName());
        response.setAvatarUrl(mediaUrlResolver.resolve(user.getAvatarUrl()));
        response.setRole(user.getRole());
        response.setAccountStatus(user.getAccountStatus());
        response.setPremiumExpiryDate(user.getPremiumExpiryDate());
        response.setLastLoginAt(user.getLastLoginAt());

        return response;
    }
}
