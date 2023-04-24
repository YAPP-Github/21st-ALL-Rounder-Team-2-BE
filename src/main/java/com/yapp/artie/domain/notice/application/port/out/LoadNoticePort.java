package com.yapp.artie.domain.notice.application.port.out;

import com.yapp.artie.domain.notice.domain.Notice;
import java.util.List;

public interface LoadNoticePort {

  List<Notice> loadNoticeList();

  Notice loadNoticeDetail(Long id);
}
