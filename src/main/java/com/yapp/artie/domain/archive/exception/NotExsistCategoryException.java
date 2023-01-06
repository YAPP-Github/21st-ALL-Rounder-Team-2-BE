package com.yapp.artie.domain.archive.exception;

import com.yapp.artie.global.exception.common.BusinessException;
import com.yapp.artie.global.exception.response.ErrorCode;

public class NotExsistCategoryException extends BusinessException {

  public NotExsistCategoryException() {
    super(ErrorCode.CATEGORY_NOT_FOUND);
  }
}
