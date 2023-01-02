package com.yapp.artie.domain.user.exception;

import com.yapp.artie.global.error.exception.BusinessException;
import com.yapp.artie.global.error.exception.ErrorCode;

public class UserAlreadyExistException extends BusinessException {

  public UserAlreadyExistException() {
    super(ErrorCode.USER_ALREADY_EXISTS);
  }
}