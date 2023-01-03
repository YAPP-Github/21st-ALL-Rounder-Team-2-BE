package com.yapp.artie.domain.user.exception;

import com.yapp.artie.global.error.exception.common.BusinessException;
import com.yapp.artie.global.error.response.ErrorCode;

public class InvalidUidException extends BusinessException {

  public InvalidUidException() {
    super(ErrorCode.AUTH_INVALID_USERINFO);
  }
}
