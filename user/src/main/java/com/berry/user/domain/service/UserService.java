package com.berry.user.domain.service;

import com.berry.user.presentation.dto.request.SignUpRequest;

public interface UserService {
    void signUp(SignUpRequest request);
}
