package com.berry.user.application.service;

import com.berry.common.exceptionhandler.CustomApiException;
import com.berry.common.response.ResErrorCode;
import com.berry.user.domain.model.User;
import com.berry.user.domain.repository.UserJpaRepository;
import com.berry.user.domain.service.UserService;
import com.berry.user.infrastructure.repository.UserQueryRepository;
import com.berry.user.presentation.dto.request.SignUpRequest;
import com.berry.user.presentation.dto.request.UpdateEmailRequest;
import com.berry.user.presentation.dto.request.UpdatePasswordRequest;
import com.berry.user.presentation.dto.response.GetInternalUserResponse;
import com.berry.user.presentation.dto.response.GetLoginUserResponse;
import com.berry.user.presentation.dto.response.GetUserDetailResponse;
import com.berry.user.presentation.dto.response.GetUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final UserJpaRepository userJpaRepository;
    private final UserQueryRepository userQueryRepository;
    private final S3UploadService s3UploadService;

    @Override
    @Transactional
    public void signUp(SignUpRequest request) {
        validateEmail(request.email());
        User user = User.create(request, passwordEncoder.encode(request.password()));

        userJpaRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public GetUserResponse getUserById(Long headerUserId, Long userId, String role) {
        if ("MEMBER".equals(role)) {
            if (!Objects.equals(headerUserId, userId)) {
                throw new CustomApiException(ResErrorCode.FORBIDDEN);
            }
        } else if (!"ADMIN".equals(role)) {
            throw new CustomApiException(ResErrorCode.FORBIDDEN);
        }

        User user = getUser(userId);
        return new GetUserResponse(
            user.getId(),
            user.getNickname(),
            user.getEmail(),
            user.getProfileImage(),
            user.getPoint(),
            user.getRole()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public GetInternalUserResponse getInternalUserById(Long userId) {
        User user = getUser(userId);
        return new GetInternalUserResponse(
            user.getId(),
            user.getNickname(),
            user.getProfileImage(),
            user.getRole()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public GetLoginUserResponse getInternalUserByNickname(String nickname) {
        User user = userJpaRepository.findByNickname(nickname)
            .orElseThrow(() -> new IllegalArgumentException(ResErrorCode.NOT_FOUND.getMessage()));
        return new GetLoginUserResponse(
            user.getId(),
            user.getNickname(),
            user.getPassword(),
            user.getRole()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GetUserDetailResponse> getUsers(Pageable pageable) {
        return userQueryRepository.getUsers(pageable);
    }

    @Override
    @Transactional
    public void updateUserEmail(Long headerUserId, Long userId, UpdateEmailRequest request) {
        validateUserSelf(headerUserId, userId);
        String newEmail = request.email();
        validateEmail(newEmail);
        User user = getUser(userId);
        user.updateEmail(newEmail, String.valueOf(userId));
    }

    @Override
    @Transactional
    public void updateUserPassword(Long headerUserId, Long userId, UpdatePasswordRequest request) {
        validateUserSelf(headerUserId, userId);
        User user = getUser(userId);

        if (!passwordEncoder.matches(request.currentPassword(), user.getPassword())) {
            throw new CustomApiException(ResErrorCode.BAD_REQUEST, "현재 비밀번호가 일치하지 않습니다.");
        }
        if (request.currentPassword().equals(request.newPassword())) {
            throw new CustomApiException(ResErrorCode.BAD_REQUEST, "새 비밀번호는 기존 비밀번호와 달라야 합니다.");
        }

        String encodedNewPassword = passwordEncoder.encode(request.newPassword());
        user.updatePassword(encodedNewPassword, String.valueOf(userId));
    }

    @Override
    public Boolean isUserIdDuplicated(String nickname) {
        return userJpaRepository.existsByNickname(nickname);
    }

    @Override
    @Transactional
    public void updateProfileImage(Long headerUserId, Long userId, MultipartFile profileImage) {
        validateUserSelf(headerUserId, userId);
        User user = getUser(headerUserId);
        String imageUrl = s3UploadService.imageUpload(profileImage);
        user.updateProfileImage(imageUrl, String.valueOf(headerUserId));
    }

    @Override
    @Transactional
    public void withdrawUser(Long headerUserId, Long userId) {
        validateUserSelf(headerUserId, userId);
        User user = getUser(userId);
        user.markAsDeleted();
    }

    private void validateEmail(String newEmail) {
        if (userJpaRepository.existsByEmail(newEmail)) {
            throw new CustomApiException(ResErrorCode.CONFLICT, "이미 사용 중인 이메일입니다.");
        }
    }

    private static void validateUserSelf(Long headerUserId, Long userId) {
        if (!Objects.equals(headerUserId, userId)) {
            throw new CustomApiException(ResErrorCode.FORBIDDEN);
        }
    }

    private User getUser(Long userId) {
        return userJpaRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException(ResErrorCode.NOT_FOUND.getMessage()));
    }
}
