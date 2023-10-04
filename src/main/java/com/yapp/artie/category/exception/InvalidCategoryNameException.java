package com.yapp.artie.category.exception;

import com.yapp.artie.global.common.exception.BusinessException;
import com.yapp.artie.global.common.exception.ErrorCode;

public class InvalidCategoryNameException extends BusinessException {

  public InvalidCategoryNameException() {
    super(ErrorCode.CATEGORY_INVALID_NAME);
  }
}
