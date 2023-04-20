package com.yapp.artie.domain.notice.adapter.out.persistence;

import com.yapp.artie.domain.notice.application.port.out.LoadNoticePort;
import com.yapp.artie.domain.notice.domain.Notice;
import com.yapp.artie.domain.notice.domain.NoticeNotFoundException;
import com.yapp.artie.global.common.annotation.PersistenceAdapter;
import java.util.List;
import lombok.AllArgsConstructor;

@PersistenceAdapter
@AllArgsConstructor
public class NoticePersistenceAdapter implements LoadNoticePort {

  final NoticeRepository noticeRepository;

  @Override
  public List<Notice> loadNoticeList() {
    return noticeRepository.findAll();
  }

  @Override
  public Notice loadNoticeDetail(Long id) {
    return noticeRepository.findById(id).orElseThrow(NoticeNotFoundException::new);
  }
}
