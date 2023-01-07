package com.yapp.artie.domain.archive.exception;

import com.yapp.artie.global.exception.common.BusinessException;
import com.yapp.artie.global.exception.response.ErrorCode;

public class ExhibitAlreadyPublishedException extends BusinessException {

  public ExhibitAlreadyPublishedException() {
    super(ErrorCode.EXHIBIT_ALREADY_PUBLISHED);
  }
}
