package com.yapp.artie.notice.domain;

import com.yapp.artie.global.common.exception.BusinessException;
import com.yapp.artie.global.common.exception.ErrorCode;

public class NoticeNotFoundException extends BusinessException {

  public NoticeNotFoundException() {
    super(ErrorCode.NOTICE_NOT_FOUND);
  }
}
