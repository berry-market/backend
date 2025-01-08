package com.berry.auth.domain.repository;

public interface AuthRepository {

  boolean saveRefreshToken(Long userId, String refreshToken);

  boolean deleteRefreshToken(String refreshToken);

  boolean addToBlacklist(String accessToken, long remainingExpiration);

  boolean isBlacklisted(String accessToken);
}
