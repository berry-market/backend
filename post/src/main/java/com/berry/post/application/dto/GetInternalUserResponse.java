package com.berry.post.application.dto;

import com.berry.common.role.Role;
public record GetInternalUserResponse(
    Long userId,
    String nickname,
    String profileImage,
    Role role
) {
}
