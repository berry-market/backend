package com.berry.user.presentation.dto.response;

import com.berry.common.role.Role;

public record GetLoginUserResponse(
    Long userId,
    String nickname,
    String password,
    Role role
) {
}
