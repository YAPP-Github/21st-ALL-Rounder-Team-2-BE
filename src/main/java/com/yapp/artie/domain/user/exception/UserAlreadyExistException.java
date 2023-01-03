package com.yapp.artie.domain.user.exception;

import com.yapp.artie.global.error.exception.common.BusinessException;
import com.yapp.artie.global.error.response.ErrorCode;

public class UserAlreadyExistException extends BusinessException {

  public UserAlreadyExistException() {
    super(ErrorCode.USER_ALREADY_EXISTS);
  }
}