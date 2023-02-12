package com.yapp.artie.domain.user.service;


import com.yapp.artie.domain.archive.service.ExhibitService;
import com.yapp.artie.domain.user.domain.User;
import com.yapp.artie.domain.user.dto.response.UserThumbnailResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserThumbnailService {

  private final UserService userService;
  private final ExhibitService exhibitService;

  public UserThumbnailResponseDto getUserThumbnail(Long id) {
    User user = userService.findById(id);
    int exhibitCount = exhibitService.getExhibitCount(user.getId());
    return new UserThumbnailResponseDto(user.getName(), exhibitCount);
  }
}
