package com.yapp.artie.user.application.service;


import com.yapp.artie.gallery.service.ExhibitionService;
import com.yapp.artie.global.common.annotation.UseCase;
import com.yapp.artie.user.application.port.in.query.GetUserThumbnailQuery;
import com.yapp.artie.user.application.port.in.response.GetUserThumbnailResponse;
import com.yapp.artie.user.application.port.out.LoadUserPort;
import com.yapp.artie.user.domain.User;
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
    int exhibitCount = exhibitionService.getExhibitionCount(user.getId());
    return new GetUserThumbnailResponse(user.getName(), exhibitCount);
  }
}
