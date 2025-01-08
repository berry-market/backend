package com.berry.user.presentation.controller;

import com.berry.common.response.ApiResponse;
import com.berry.common.response.ResSuccessCode;
import com.berry.common.role.RoleCheck;
import com.berry.user.domain.service.UserService;
import com.berry.user.presentation.dto.response.GetUserDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/v1/users")
public class AdminUserController {

    private final UserService userService;

    @GetMapping
    @RoleCheck("ADMIN")
    public ResponseEntity<ApiResponse<Page<GetUserDetailResponse>>> getUsers(
        @RequestHeader("X-User-Role") String role,
        Pageable pageable
    ) {
        return ResponseEntity.ok(ApiResponse.OK(ResSuccessCode.READ, userService.getUsers(pageable)));
    }

}
