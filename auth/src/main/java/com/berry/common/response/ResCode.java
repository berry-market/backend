package com.berry.common.response;

public interface ResCode {

  Integer getHttpStatusCode();
  Integer getCode();
  String getMessage();
}