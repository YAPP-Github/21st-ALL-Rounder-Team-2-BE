package com.yapp.artie.user.application.port.in.query;

import com.yapp.artie.user.application.port.in.response.GetUserThumbnailResponse;

public interface GetUserThumbnailQuery {

  GetUserThumbnailResponse loadUserThumbnailById(Long id);
}
