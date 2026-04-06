package com.hoaug.movieapi.modules.user.application.usecase;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.user.application.dto.request.UpdateUserRoleRequest;
import com.hoaug.movieapi.modules.user.application.dto.response.UserDetailResponse;
import com.hoaug.movieapi.modules.user.application.mapper.UserMapper;
import com.hoaug.movieapi.modules.user.domain.model.User;
import com.hoaug.movieapi.modules.user.domain.repository.UserRepository;

@Component
public class UpdateUserRoleUseCase {
    public final UserRepository userRepository;
    public final UserMapper userMapper;

    public UpdateUserRoleUseCase(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UserDetailResponse execute(Long userId, UpdateUserRoleRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        user.setRole(request.getRole());

        User savedUser = userRepository.save(user);
        return userMapper.toDetailResponse(savedUser);
    }
}
