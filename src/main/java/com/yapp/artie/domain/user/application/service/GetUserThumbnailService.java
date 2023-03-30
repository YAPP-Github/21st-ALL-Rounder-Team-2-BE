package com.yapp.artie.domain.user.application.service;


import com.yapp.artie.domain.archive.service.ExhibitService;
import com.yapp.artie.domain.user.application.port.in.GetUserThumbnailQuery;
import com.yapp.artie.domain.user.application.port.out.LoadUserPort;
import com.yapp.artie.domain.user.domain.User;
import com.yapp.artie.domain.user.dto.response.UserThumbnailResponseDto;
import com.yapp.artie.global.annotation.UseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GetUserThumbnailService implements GetUserThumbnailQuery {

  private final LoadUserPort loadUserPort;
  private final ExhibitService exhibitService;

  @Override
  public UserThumbnailResponseDto loadUserThumbnailById(Long id) {
    User user = loadUserPort.loadById(id);
    int exhibitCount = exhibitService.getExhibitCount(user.getId());
    return new UserThumbnailResponseDto(user.getName(), exhibitCount);
  }
}