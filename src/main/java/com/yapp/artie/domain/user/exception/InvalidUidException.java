package com.yapp.artie.domain.user.exception;

import com.yapp.artie.global.exception.common.BusinessException;
import com.yapp.artie.global.exception.response.ErrorCode;

public class InvalidUidException extends BusinessException {

  public InvalidUidException() {
    super(ErrorCode.FIREBASE_INVALID_USERINFO);
  }
}
