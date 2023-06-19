package com.yapp.artie.domain.category.exception;

import com.yapp.artie.global.common.exception.BusinessException;
import com.yapp.artie.global.common.exception.ErrorCode;

public class CategoryAlreadyExistException extends BusinessException {

  public CategoryAlreadyExistException() {
    super(ErrorCode.CATEGORY_ALREADY_EXISTS);
  }
}
