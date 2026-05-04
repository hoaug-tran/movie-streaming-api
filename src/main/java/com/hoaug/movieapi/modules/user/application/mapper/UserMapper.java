package com.hoaug.movieapi.modules.user.application.mapper;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.user.application.dto.response.UserDetailResponse;
import com.hoaug.movieapi.modules.user.application.dto.response.UserProfileResponse;
import com.hoaug.movieapi.modules.user.application.dto.response.UserSummaryResponse;
import com.hoaug.movieapi.modules.user.domain.model.User;
import com.hoaug.movieapi.shared.media.MediaUrlResolver;

@Component
public class UserMapper {
    private final MediaUrlResolver mediaUrlResolver;

    public UserMapper(MediaUrlResolver mediaUrlResolver) {
        this.mediaUrlResolver = mediaUrlResolver;
    }

    public UserProfileResponse toProfileResponse(User user) {
        UserProfileResponse response = new UserProfileResponse();

        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setFullName(user.getFullName());
        response.setAvatarUrl(mediaUrlResolver.resolve(user.getAvatarUrl()));
        response.setRole(user.getRole());
        response.setAccountStatus(user.getAccountStatus());
        response.setPremiumExpiryDate(user.getPremiumExpiryDate());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());
        response.setLastLoginAt(user.getLastLoginAt());

        return response;
    }

    public UserSummaryResponse toSummaryResponse(User user) {
        UserSummaryResponse response = new UserSummaryResponse();

        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setFullName(user.getFullName());
        response.setRole(user.getRole());
        response.setAccountStatus(user.getAccountStatus());

        return response;
    }

    public UserDetailResponse toDetailResponse(User user) {
        UserDetailResponse response = new UserDetailResponse();

        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setFullName(user.getFullName());
        response.setAvatarUrl(mediaUrlResolver.resolve(user.getAvatarUrl()));
        response.setRole(user.getRole());
        response.setAccountStatus(user.getAccountStatus());
        response.setPremiumExpiryDate(user.getPremiumExpiryDate());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());
        response.setLastLoginAt(user.getLastLoginAt());

        return response;
    }

}
