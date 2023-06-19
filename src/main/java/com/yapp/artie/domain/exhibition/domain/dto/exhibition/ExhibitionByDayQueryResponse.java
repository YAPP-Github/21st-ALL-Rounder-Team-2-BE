package com.yapp.artie.domain.exhibition.domain.dto.exhibition;

/* 주어진 기간 동안의 일별 전시 정보, 같은 날에 등록된 전시가 여러개라면, 가장 먼저 등록된 전시 정보를 반환함. */
public interface ExhibitionByDayQueryResponse {

  String getCalenderDate();

  Long getPostId();

  Long getPostNum();

  String getUri();
}
