package com.berry.user.presentation.dto.response;

import com.berry.common.role.Role;

public record GetInternalUserResponse(
    Long userId,
    String nickname,
    String profileImage,
    Role role
) {
}