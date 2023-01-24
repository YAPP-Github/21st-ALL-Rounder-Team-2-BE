package com.yapp.artie.domain.archive.exception;

import com.yapp.artie.global.exception.common.BusinessException;
import com.yapp.artie.global.exception.response.ErrorCode;

public class TagNotFoundException extends BusinessException {

  public TagNotFoundException() {
    super(ErrorCode.TAG_NOT_FOUND);
  }
}
