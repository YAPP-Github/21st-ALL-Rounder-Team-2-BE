package com.yapp.artie.domain.notice.adapter.out.persistence;

import com.yapp.artie.domain.notice.domain.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface NoticeRepository extends JpaRepository<Notice, Long> {

}
