package com.berry.user.application.service;

import com.berry.common.exceptionhandler.CustomApiException;
import com.berry.common.response.ResErrorCode;
import com.berry.common.role.Role;
import com.berry.user.domain.model.User;
import com.berry.user.domain.repository.UserJpaRepository;
import com.berry.user.domain.service.UserService;
import com.berry.user.infrastructure.repository.UserQueryRepository;
import com.berry.user.presentation.dto.request.SignUpRequest;
import com.berry.user.presentation.dto.request.UpdateEmailRequest;
import com.berry.user.presentation.dto.response.GetInternalUserResponse;
import com.berry.user.presentation.dto.response.GetUserDetailResponse;
import com.berry.user.presentation.dto.response.GetUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final UserJpaRepository userJpaRepository;
    private final UserQueryRepository userQueryRepository;

    @Override
    @Transactional
    public void signUp(SignUpRequest request) {
        validateEmail(request.email());
        String encodedPassword = passwordEncoder.encode(request.password());
        User user = User.builder()
            .nickname(request.nickname())
            .email(request.email())
            .password(encodedPassword)
            .role(Role.MEMBER)
            .build();

        userJpaRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public GetUserResponse getUserById(Long headerUserId, Long userId, String role) {
        if (!Objects.equals(headerUserId, userId) || !Objects.equals(role, "ADMIN")) {
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
        return getInternalUserResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public GetInternalUserResponse getInternalUserByNickname(String nickname) {
        User user = userJpaRepository.findByNickname(nickname)
            .orElseThrow(() -> new IllegalArgumentException(ResErrorCode.NOT_FOUND.getMessage()));
        return getInternalUserResponse(user);
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

    private static GetInternalUserResponse getInternalUserResponse(User user) {
        return new GetInternalUserResponse(
            user.getId(),
            user.getNickname(),
            user.getProfileImage(),
            user.getRole()
        );
    }
}
