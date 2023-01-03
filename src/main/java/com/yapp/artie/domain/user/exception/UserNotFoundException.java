package com.yapp.artie.domain.user.exception;


import com.yapp.artie.global.error.exception.common.EntityNotFoundException;
import com.yapp.artie.global.error.response.ErrorCode;

public class UserNotFoundException extends EntityNotFoundException {

  public UserNotFoundException() {
    super(ErrorCode.USER_NOT_FOUND.getMessage());
  }
}