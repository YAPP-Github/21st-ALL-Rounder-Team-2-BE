package com.yapp.artie.domain.notice.service;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.yapp.artie.domain.notice.domain.Notice;
import com.yapp.artie.domain.notice.dto.NoticeDetailInfo;
import com.yapp.artie.domain.notice.dto.NoticeDto;
import com.yapp.artie.domain.notice.exception.NoticeNotFoundException;
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

  @Test
  public void notice_공지사항_상세를_조회한다() throws Exception {
    String expectedContents = "sample data";
    Notice notice = Notice.create("test1", expectedContents);
    em.persist(notice);
    NoticeDetailInfo detail = noticeService.notice(notice.getId());
    assertThat(detail.getContents()).isEqualTo(expectedContents);
  }

  @Test
  public void notice_존재하지_않는_공지사항을_조회_시도할_경우_예외를_발생() throws Exception {
    String expectedContents = "sample data";
    Notice notice = Notice.create("test1", expectedContents);
    em.persist(notice);
    assertThatThrownBy(() -> {
      noticeService.notice(22L);
    }).isInstanceOf(NoticeNotFoundException.class);
  }
}