package com.yapp.artie.domain.notice.repository;

import com.yapp.artie.domain.notice.application.port.in.GetNoticeDetailResponse;
import com.yapp.artie.domain.notice.domain.Notice;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {

  @Query("select new com.yapp.artie.domain.notice.dto.GetNoticeDetailResponse(n.id, n.createdAt, n.title, n.contents) from Notice n")
  List<GetNoticeDetailResponse> findNoticeDto();
}
