package com.yapp.artie.domain.user.application.port.in.query;

import java.time.LocalDateTime;

public interface GetUserJoinDateQuery {

  LocalDateTime loadUserJoinDate(Long userId);
}
