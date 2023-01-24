package com.yapp.artie.domain.archive.exception;

import com.yapp.artie.global.exception.common.BusinessException;
import com.yapp.artie.global.exception.response.ErrorCode;

public class CategoryNotFoundException extends BusinessException {

  public CategoryNotFoundException() {
    super(ErrorCode.CATEGORY_NOT_FOUND);
  }
}
