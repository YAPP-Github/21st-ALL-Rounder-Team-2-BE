package com.yapp.artie.domain.user.adapter.out.authentication;

import com.yapp.artie.global.common.exception.BusinessException;
import com.yapp.artie.global.common.exception.ErrorCode;

class NotExistValidTokenException extends BusinessException {

  public NotExistValidTokenException() {
    super(ErrorCode.FIREBASE_TOKEN_NOT_EXISTS);
  }
}
