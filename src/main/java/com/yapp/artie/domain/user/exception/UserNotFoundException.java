package com.yapp.artie.domain.user.exception;


import com.yapp.artie.global.exception.common.BusinessException;
import com.yapp.artie.global.exception.response.ErrorCode;

public class UserNotFoundException extends BusinessException {

  public UserNotFoundException() {
    super(ErrorCode.USER_NOT_FOUND);
  }
}