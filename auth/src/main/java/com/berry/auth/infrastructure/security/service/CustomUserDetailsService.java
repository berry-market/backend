package com.berry.auth.infrastructure.security.service;

import com.berry.auth.domain.model.Role;
import com.berry.auth.infrastructure.security.dto.CustomUserDetails;
import com.berry.auth.infrastructure.security.dto.UserResDto;
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

    UserResDto userResDto;

     // TODO: User Service 연동
//    try {
//      // 유저서비스에서 유저 정보 가져오기
//      userResDto = userClient.getUserByNickname(nickname).getData();
//    } catch (FeignException e) {
//      throw new CustomApiException(ResErrorCode.API_CALL_FAILED,
//          "User Service: " + e.getMessage());
//    }
//
//    if (userResDto == null) {
//      throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
//    }

     userResDto = new UserResDto(1L, "user1", Role.MEMBER, "$2a$10$wjcbslxcYk5s4hkHLIdtZOnwfmxDCnH4Y82hRlcLLG9Qq0uCw02aW");

      // UserDetails 객체 반환
      return new CustomUserDetails(
          userResDto.getId(),
          userResDto.getNickname(),
          userResDto.getPassword(),
          new SimpleGrantedAuthority(userResDto.getRole().name())
      );
  }
}
