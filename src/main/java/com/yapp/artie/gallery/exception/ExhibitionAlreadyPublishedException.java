package com.yapp.artie.gallery.exception;

import com.yapp.artie.global.common.exception.BusinessException;
import com.yapp.artie.global.common.exception.ErrorCode;

public class ExhibitionAlreadyPublishedException extends BusinessException {

  public ExhibitionAlreadyPublishedException() {
    super(ErrorCode.EXHIBITION_ALREADY_PUBLISHED);
  }
}
