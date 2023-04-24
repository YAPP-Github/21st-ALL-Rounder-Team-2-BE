package com.yapp.artie.domain.notice.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.yapp.artie.domain.notice.domain.Notice;
import com.yapp.artie.domain.notice.domain.NoticeNotFoundException;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@Import({NoticePersistenceAdapter.class})
@AutoConfigureTestDatabase(replace = Replace.NONE)
class NoticePersistenceAdapterTest {

  @Autowired
  NoticePersistenceAdapter noticePersistenceAdapter;

  @Autowired
  NoticeRepository noticeRepository;

  @Test
  @Sql("NoticePersistenceAdapterTest.sql")
  void loadNoticeList_모든_공지_조회() {
    List<Notice> notices = noticePersistenceAdapter.loadNoticeList();

    assertThat(notices.size()).isEqualTo(6);
    assertThat(notices.get(0).getId()).isEqualTo(1L);
  }

  @Test
  void loadNoticeList_공지가_없을_경우_빈_리스트를_반환한다() {
    List<Notice> notices = noticePersistenceAdapter.loadNoticeList();

    assertThat(notices.size()).isEqualTo(0);
  }


  @Test
  @Sql("NoticePersistenceAdapterTest.sql")
  void loadNoticeDetail_공지_ID를_이용해서_공지_세부_내용을_조회한다() {
    Notice notice = noticePersistenceAdapter.loadNoticeDetail(1L);

    assertThat(notice.getId()).isEqualTo(1L);
  }

  @Test
  void loadNoticeDetail_공지_ID를_찾지_못한_경우_예외를_발생한다() {
    assertThatThrownBy(() -> {
      noticePersistenceAdapter.loadNoticeDetail(1L);
    }).isInstanceOf(
        NoticeNotFoundException.class);
  }
}