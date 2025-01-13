package com.berry.user.presentation.controller;

import com.berry.common.response.ApiResponse;
import com.berry.common.response.ResSuccessCode;
import com.berry.user.domain.service.UserService;
import com.berry.user.presentation.dto.request.SignUpRequest;
import com.berry.user.presentation.dto.request.UpdateEmailRequest;
import com.berry.user.presentation.dto.request.UpdatePasswordRequest;
import com.berry.user.presentation.dto.response.GetUserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/sign-up")
    public ApiResponse<Void> signUp(@Valid @RequestBody SignUpRequest request) {
        userService.signUp(request);
        return ApiResponse.OK(ResSuccessCode.CREATED, "회원가입이 완료되었습니다.");
    }

    @GetMapping("/{userId}")
    public ApiResponse<GetUserResponse> getUserById(
        @RequestHeader("X-UserId") Long headerUserId,
        @RequestHeader("X-Role") String role,
        @PathVariable("userId") Long userId
    ) {
        return ApiResponse.OK(ResSuccessCode.READ, userService.getUserById(headerUserId, userId, role));
    }

    @PatchMapping("/{userId}/email")
    public ApiResponse<Void> updateUserEmail(
        @RequestHeader("X-UserId") Long headerUserId,
        @PathVariable("userId") Long userId,
        @RequestBody @Valid UpdateEmailRequest request
    ) {
        userService.updateUserEmail(headerUserId, userId, request);
        return ApiResponse.OK(ResSuccessCode.UPDATED, "이메일이 성공적으로 변경되었습니다.");
    }

    @PatchMapping("/{userId}/password")
    public ApiResponse<Void> updateUserPassword(
        @RequestHeader("X-UserId") Long headerUserId,
        @PathVariable("userId") Long userId,
        @RequestBody @Valid UpdatePasswordRequest request
    ) {
        userService.updateUserPassword(headerUserId, userId, request);
        return ApiResponse.OK(ResSuccessCode.UPDATED, "비밀번호가 성공적으로 변경되었습니다.");
    }

    @GetMapping("/check-id/{userId}")
    public ApiResponse<Boolean> checkUserIdDuplication(@PathVariable("userId") String userId) {
        return ApiResponse.OK(ResSuccessCode.READ, userService.isUserIdDuplicated(userId));
    }

    @PatchMapping("/{userId}/profile-image")
    public ApiResponse<Void> updateProfileImage(
        @RequestHeader("X-UserId") Long headerUserId,
        @PathVariable("userId") Long userId,
        @RequestPart(value = "profileImage") MultipartFile profileImage
    ) {
        userService.updateProfileImage(headerUserId, userId, profileImage);
        return ApiResponse.OK(ResSuccessCode.UPDATED, "프로필 이미지가 성공적으로 수정되었습니다.");
    }

    @DeleteMapping("/withdraw/{userId}")
    public ApiResponse<Void> withdrawUser(
        @RequestHeader("X-UserId") Long headerUserId,
        @PathVariable("userId") Long userId
    ) {
        userService.withdrawUser(headerUserId, userId);
        return ApiResponse.OK(ResSuccessCode.DELETED, "회원 탈퇴가 완료되었습니다.");
    }

}
