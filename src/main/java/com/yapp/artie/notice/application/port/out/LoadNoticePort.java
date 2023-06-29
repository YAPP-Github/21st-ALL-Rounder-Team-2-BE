package com.yapp.artie.notice.application.port.out;

import com.yapp.artie.notice.domain.Notice;
import java.util.List;

public interface LoadNoticePort {

  List<Notice> loadNoticeList();

  Notice loadNoticeDetail(Long id);
}
