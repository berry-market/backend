package com.berry.common.exceptionhandler;

import com.berry.common.response.ResCode;
import lombok.Getter;

@Getter
public class CustomApiException extends RuntimeException {

  private final ResCode errorCode;
  private final String errorDescription;

  public CustomApiException(ResCode errorCode) {
    this(errorCode, errorCode.getMessage());
  }

  public CustomApiException(ResCode errorCode, String errorDescription) {
    super(errorDescription);
    this.errorCode = errorCode;
    this.errorDescription = errorDescription;
  }
}