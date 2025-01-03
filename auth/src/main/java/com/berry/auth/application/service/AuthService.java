package com.berry.auth.application.service;

public interface AuthService {

  String refreshAccessToken(String refreshToken);

}
