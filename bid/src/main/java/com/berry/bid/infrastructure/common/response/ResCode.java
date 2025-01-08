package com.berry.bid.infrastructure.common.response;

public interface ResCode {

  Integer getHttpStatusCode();
  Integer getCode();
  String getMessage();
}