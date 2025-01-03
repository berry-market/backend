package com.berry.user.application.service;

import com.berry.common.exceptionhandler.CustomApiException;
import com.berry.common.response.ResErrorCode;
import com.berry.common.role.Role;
import com.berry.user.domain.model.User;
import com.berry.user.domain.repository.UserJpaRepository;
import com.berry.user.domain.service.UserService;
import com.berry.user.presentation.dto.request.SignUpRequest;
import com.berry.user.presentation.dto.response.GetInternalUserResponse;
import com.berry.user.presentation.dto.response.GetUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final UserJpaRepository userJpaRepository;

    @Override
    @Transactional
    public void signUp(SignUpRequest request) {
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
        return new GetInternalUserResponse(
            user.getId(),
            user.getNickname(),
            user.getProfileImage(),
            user.getRole()
        );
    }

    private User getUser(Long userId) {
        return userJpaRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException(ResErrorCode.NOT_FOUND.getMessage()));
    }
}
