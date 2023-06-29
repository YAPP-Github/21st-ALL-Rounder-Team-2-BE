package com.yapp.artie.notice.adapter.out.persistence;

import com.yapp.artie.notice.domain.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

interface NoticeRepository extends JpaRepository<Notice, Long> {

}
