package com.berry.bid.infrastructure.model.dto;

import com.berry.common.role.Role;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserInternalView {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Response {
        Long userId;
        String nickname;
        String profileImage;
        Role role;
    }
}
