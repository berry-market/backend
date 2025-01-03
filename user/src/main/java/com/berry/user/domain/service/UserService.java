package com.berry.user.domain.service;

import com.berry.user.presentation.dto.request.SignUpRequest;
import com.berry.user.presentation.dto.response.GetUserResponse;

public interface UserService {
    void signUp(SignUpRequest request);

    GetUserResponse getUserById(Long headerUserId, Long userId, String role);
}
