package com.yapp.artie.domain.user.application.port.exception;

import com.yapp.artie.global.exception.common.BusinessException;
import com.yapp.artie.global.exception.response.ErrorCode;

public class UserAlreadyExistException extends BusinessException {

  public UserAlreadyExistException() {
    super(ErrorCode.USER_ALREADY_EXISTS);
  }
}