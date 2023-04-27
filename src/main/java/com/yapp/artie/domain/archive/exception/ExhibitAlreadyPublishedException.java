package com.yapp.artie.domain.archive.exception;

import com.yapp.artie.global.common.exception.BusinessException;
import com.yapp.artie.global.common.exception.ErrorCode;

public class ExhibitAlreadyPublishedException extends BusinessException {

  public ExhibitAlreadyPublishedException() {
    super(ErrorCode.EXHIBIT_ALREADY_PUBLISHED);
  }
}
