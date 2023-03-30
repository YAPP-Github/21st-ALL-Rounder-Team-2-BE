package com.yapp.artie.domain.user.application.port.in;

import com.yapp.artie.domain.user.dto.response.UserThumbnailResponseDto;

public interface GetUserThumbnailQuery {

  UserThumbnailResponseDto loadUserThumbnailById(Long id);
}
