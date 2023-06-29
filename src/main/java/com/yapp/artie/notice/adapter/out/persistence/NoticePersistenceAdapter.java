package com.yapp.artie.notice.adapter.out.persistence;

import com.yapp.artie.global.common.annotation.PersistenceAdapter;
import com.yapp.artie.notice.application.port.out.LoadNoticePort;
import com.yapp.artie.notice.domain.Notice;
import com.yapp.artie.notice.domain.NoticeNotFoundException;
import java.util.List;
import lombok.AllArgsConstructor;

@PersistenceAdapter
@AllArgsConstructor
class NoticePersistenceAdapter implements LoadNoticePort {

  private final NoticeRepository noticeRepository;

  @Override
  public List<Notice> loadNoticeList() {
    return noticeRepository.findAll();
  }

  @Override
  public Notice loadNoticeDetail(Long id) {
    return noticeRepository.findById(id).orElseThrow(NoticeNotFoundException::new);
  }
}
