package com.yapp.artie.domain.user.application.service;

import com.yapp.artie.global.common.exception.BusinessException;
import com.yapp.artie.global.common.exception.ErrorCode;

public class UserAlreadyExistException extends BusinessException {

  public UserAlreadyExistException() {
    super(ErrorCode.USER_ALREADY_EXISTS);
  }
}