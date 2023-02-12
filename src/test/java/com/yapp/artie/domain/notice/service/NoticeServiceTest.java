package com.yapp.artie.domain.notice.service;


import static org.assertj.core.api.Assertions.assertThat;

import com.yapp.artie.domain.notice.domain.Notice;
import com.yapp.artie.domain.notice.dto.NoticeDto;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class NoticeServiceTest {

  @Autowired
  EntityManager em;

  @Autowired
  NoticeService noticeService;

  @Test
  public void notices_공지사항_목록을_조회한다() throws Exception {
    for (int i = 0; i < 4; i++) {
      Notice notice = Notice.create("test " + i, "sample data");
      em.persist(notice);
    }
    List<NoticeDto> notices = noticeService.notices();
    assertThat(notices.size()).isEqualTo(4);
  }
}