package com.yapp.artie.global.error.exception.authentication;

import com.yapp.artie.global.error.exception.common.BusinessException;
import com.yapp.artie.global.error.response.ErrorCode;

public class InvalidTokenException extends BusinessException {

  public InvalidTokenException() {
    super(ErrorCode.USER_TOKEN_ERROR);
  }
}
