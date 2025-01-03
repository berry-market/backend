package com.berry.user.application.service;

import com.berry.common.role.Role;
import com.berry.user.domain.model.User;
import com.berry.user.domain.repository.UserJpaRepository;
import com.berry.user.domain.service.UserService;
import com.berry.user.presentation.dto.request.SignUpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

}
