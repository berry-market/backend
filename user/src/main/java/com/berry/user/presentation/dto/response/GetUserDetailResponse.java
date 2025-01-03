package com.berry.user.presentation.dto.response;

import com.berry.common.role.Role;

public record GetUserDetailResponse(
    Long userId,
    String nickname,
    String email,
    String profileImage,
    int point,
    Role role,
    boolean deletedYn
) {
}
