package com.yapp.artie.domain.archive.exception;

import com.yapp.artie.global.exception.common.BusinessException;
import com.yapp.artie.global.exception.response.ErrorCode;

public class ChangeDefaultCategoryException extends BusinessException {

  public ChangeDefaultCategoryException() {
    super(ErrorCode.CATEGORY_DEFAULT_IS_READONLY);
  }
}
