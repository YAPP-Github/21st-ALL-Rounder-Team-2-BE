package com.yapp.artie.domain.category.exception;

import com.yapp.artie.global.common.exception.BusinessException;
import com.yapp.artie.global.common.exception.ErrorCode;

public class CategoryNotFoundException extends BusinessException {

  public CategoryNotFoundException() {
    super(ErrorCode.CATEGORY_NOT_FOUND);
  }
}
