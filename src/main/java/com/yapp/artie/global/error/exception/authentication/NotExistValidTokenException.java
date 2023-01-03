package com.yapp.artie.global.error.exception.authentication;

import com.yapp.artie.global.error.exception.common.BusinessException;
import com.yapp.artie.global.error.response.ErrorCode;

public class NotExistValidTokenException extends BusinessException {

  public NotExistValidTokenException() {
    super(ErrorCode.AUTH_TOKEN_NOT_EXISTS);
  }
}
