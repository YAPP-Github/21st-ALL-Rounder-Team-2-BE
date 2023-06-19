package com.yapp.artie.domain.exhibition.domain.exception;

import com.yapp.artie.global.common.exception.BusinessException;
import com.yapp.artie.global.common.exception.ErrorCode;

public class NotOwnerOfArtworkException extends BusinessException {

  public NotOwnerOfArtworkException() {
    super(ErrorCode.ARTWORK_NOT_OWNER);
  }
}
