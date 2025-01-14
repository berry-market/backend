package com.berry.user.domain.service;

import com.berry.user.presentation.dto.request.SignUpRequest;
import com.berry.user.presentation.dto.request.UpdateEmailRequest;
import com.berry.user.presentation.dto.request.UpdatePasswordRequest;
import com.berry.user.presentation.dto.response.GetInternalUserResponse;
import com.berry.user.presentation.dto.response.GetLoginUserResponse;
import com.berry.user.presentation.dto.response.GetUserDetailResponse;
import com.berry.user.presentation.dto.response.GetUserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    void signUp(SignUpRequest request);

    GetUserResponse getUserById(Long headerUserId, Long userId, String role);

    GetInternalUserResponse getInternalUserById(Long userId);

    GetLoginUserResponse getInternalUserByNickname(String nickname);

    Page<GetUserDetailResponse> getUsers(Pageable pageable);

    void updateUserEmail(Long headerUserId, Long userId, UpdateEmailRequest request);

    void updateUserPassword(Long headerUserId, Long userId, UpdatePasswordRequest request);

    Boolean isUserIdDuplicated(String nickname);

    void updateProfileImage(Long headerUserId, Long userId, MultipartFile profileImage);

    void withdrawUser(Long headerUserId, Long userId);
}
