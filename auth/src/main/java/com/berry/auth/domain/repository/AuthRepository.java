package com.berry.auth.domain.repository;

public interface AuthRepository {

  boolean saveRefreshToken(Long userId, String refreshToken);

  boolean deleteRefreshToken(Long userId);

  boolean addToBlacklist(String accessToken, long remainingExpiration);

  boolean isBlacklisted(String accessToken);
}
