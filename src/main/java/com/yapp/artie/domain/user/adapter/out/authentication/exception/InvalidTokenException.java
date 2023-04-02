package com.yapp.artie.domain.user.adapter.out.authentication.exception;

import com.yapp.artie.global.exception.common.BusinessException;
import com.yapp.artie.global.exception.response.ErrorCode;

public class InvalidTokenException extends BusinessException {

  public InvalidTokenException() {
    super(ErrorCode.FIREBASE_INVALID_TOKEN);
  }
}
