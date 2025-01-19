package com.berry.auth.infrastructure.security.service;

import com.berry.auth.domain.model.Role;
import com.berry.auth.infrastructure.security.dto.CustomUserDetails;
import com.berry.auth.infrastructure.security.dto.UserLoginResDto;
import com.berry.auth.infrastructure.client.UserClient;
import com.berry.common.exceptionhandler.CustomApiException;
import com.berry.common.response.ResErrorCode;
import feign.FeignException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

  private final UserClient userClient;

  public CustomUserDetailsService(UserClient userClient) {
    this.userClient = userClient;
  }

  @Override
  public UserDetails loadUserByUsername(String nickname) throws UsernameNotFoundException {

    UserLoginResDto user;

    try {
      user = userClient.getUserByNickname(nickname).getData();
    } catch (FeignException e) {
      throw new CustomApiException(ResErrorCode.API_CALL_FAILED,
          "User Service: " + e.getMessage());
    }

    if (user == null) {
      throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
    }
      return new CustomUserDetails(
          user.getUserId(),
          user.getNickname(),
          user.getPassword(),
          new SimpleGrantedAuthority(user.getRole().name())
      );
  }
}
