package com.yapp.artie.domain.archive.exception;


import com.yapp.artie.global.common.exception.BusinessException;
import com.yapp.artie.global.common.exception.ErrorCode;

public class ArtworkNotFoundException extends BusinessException {

  public ArtworkNotFoundException() {
    super(ErrorCode.ARTWORK_NOT_FOUND);
  }
}
