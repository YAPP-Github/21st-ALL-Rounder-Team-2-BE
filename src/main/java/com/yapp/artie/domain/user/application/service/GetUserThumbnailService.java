package com.yapp.artie.domain.user.application.service;


import com.yapp.artie.domain.archive.service.ExhibitService;
import com.yapp.artie.domain.user.adapter.out.persistence.UserJpaEntity;
import com.yapp.artie.domain.user.dto.response.UserThumbnailResponseDto;
import com.yapp.artie.domain.user.service.UserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserThumbnailService {

  private final UserUseCase userService;
  private final ExhibitService exhibitService;

  public UserThumbnailResponseDto getUserThumbnail(Long id) {
    UserJpaEntity user = userService.findById(id);
    int exhibitCount = exhibitService.getExhibitCount(user.getId());
    return new UserThumbnailResponseDto(user.getName(), exhibitCount);
  }
}
