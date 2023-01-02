package com.yapp.artie.domain.user.exception;


import com.yapp.artie.global.error.exception.EntityNotFoundException;
import com.yapp.artie.global.error.exception.ErrorCode;

public class UserNotFoundException extends EntityNotFoundException {

  public UserNotFoundException() {
    super(ErrorCode.USER_NOT_FOUND.getMessage());
  }
}