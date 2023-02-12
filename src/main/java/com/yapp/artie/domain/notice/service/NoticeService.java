package com.yapp.artie.domain.notice.service;

import com.yapp.artie.domain.notice.dto.NoticeDto;
import com.yapp.artie.domain.notice.repository.NoticeRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NoticeService {

  private final NoticeRepository noticeRepository;

  public List<NoticeDto> notices() {
    return noticeRepository.findNoticeDto();
  }
}
