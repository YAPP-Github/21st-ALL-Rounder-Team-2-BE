package com.yapp.artie.domain.notice.application.port.out;

import com.yapp.artie.domain.notice.domain.Notice;
import java.util.List;
import java.util.Optional;

public interface LoadNoticePort {

  List<Notice> loadNoticeList();

  Optional<Notice> loadNoticeDetail(Long id);
}
