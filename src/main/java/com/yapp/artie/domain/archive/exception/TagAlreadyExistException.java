package com.yapp.artie.domain.archive.exception;

import com.yapp.artie.global.exception.common.BusinessException;
import com.yapp.artie.global.exception.response.ErrorCode;

public class TagAlreadyExistException extends BusinessException {

  public TagAlreadyExistException() {
    super(ErrorCode.TAG_ALREADY_EXISTS);
  }
}
