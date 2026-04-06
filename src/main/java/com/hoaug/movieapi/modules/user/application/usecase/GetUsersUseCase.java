package com.hoaug.movieapi.modules.user.application.usecase;

import java.util.List;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.user.application.dto.response.UserSummaryResponse;
import com.hoaug.movieapi.modules.user.application.mapper.UserMapper;
import com.hoaug.movieapi.modules.user.domain.repository.UserRepository;

@Component
public class GetUsersUseCase {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public GetUsersUseCase(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public List<UserSummaryResponse> execute() {

        return userRepository.findAll().stream().map(userMapper::toSummaryResponse).toList();
    }

}
