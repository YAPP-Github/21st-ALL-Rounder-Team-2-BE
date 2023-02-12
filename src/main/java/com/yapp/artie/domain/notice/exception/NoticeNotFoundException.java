package com.yapp.artie.domain.notice.exception;

import com.yapp.artie.global.exception.common.BusinessException;
import com.yapp.artie.global.exception.response.ErrorCode;

public class NoticeNotFoundException extends BusinessException {

  public NoticeNotFoundException() {
    super(ErrorCode.NOTICE_NOT_FOUND);
  }
}
