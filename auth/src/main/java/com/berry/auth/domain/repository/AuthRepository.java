package com.berry.auth.domain.repository;

public interface AuthRepository {

  boolean saveRefreshToken(Long userId, String refreshToken);

}
