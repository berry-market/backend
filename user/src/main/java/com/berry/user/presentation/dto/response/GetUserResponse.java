package com.berry.user.presentation.dto.response;

import com.berry.common.role.Role;

public record GetUserResponse(
    Long userId,
    String nickname,
    String email,
    String profileImage,
    int point,
    Role role
) {
}
