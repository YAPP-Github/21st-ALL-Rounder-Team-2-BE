package com.yapp.artie.domain.user.application.port.in;

public interface GetUserThumbnailQuery {

  GetUserThumbnailResponse loadUserThumbnailById(Long id);
}
