package com.yapp.artie.domain.user.exception;

import com.yapp.artie.global.error.exception.BusinessException;
import com.yapp.artie.global.error.exception.ErrorCode;

public class InvalidUidException extends BusinessException {

  public InvalidUidException() {
    super(ErrorCode.AUTH_INVALID_USERINFO);
  }
}
