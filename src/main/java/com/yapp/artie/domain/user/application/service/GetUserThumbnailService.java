package com.yapp.artie.domain.user.application.service;


import com.yapp.artie.domain.exhibition.domain.service.ExhibitionService;
import com.yapp.artie.domain.user.application.port.in.query.GetUserThumbnailQuery;
import com.yapp.artie.domain.user.application.port.in.response.GetUserThumbnailResponse;
import com.yapp.artie.domain.user.application.port.out.LoadUserPort;
import com.yapp.artie.domain.user.domain.User;
import com.yapp.artie.global.common.annotation.UseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@Transactional(readOnly = true)
@RequiredArgsConstructor
class GetUserThumbnailService implements GetUserThumbnailQuery {

  private final LoadUserPort loadUserPort;
  private final ExhibitionService exhibitionService;

  @Override
  public GetUserThumbnailResponse loadUserThumbnailById(Long id) {
    User user = loadUserPort.loadById(id);
    int exhibitCount = exhibitionService.getExhibitCount(user.getId());
    return new GetUserThumbnailResponse(user.getName(), exhibitCount);
  }
}
