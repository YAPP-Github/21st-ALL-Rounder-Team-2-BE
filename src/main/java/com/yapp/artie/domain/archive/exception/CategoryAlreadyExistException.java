package com.yapp.artie.domain.archive.exception;

import com.yapp.artie.global.exception.common.BusinessException;
import com.yapp.artie.global.exception.response.ErrorCode;

public class CategoryAlreadyExistException extends BusinessException {

  public CategoryAlreadyExistException() {
    super(ErrorCode.CATEGORY_ALREADY_EXISTS);
  }
}
