package com.yapp.artie.domain.user.application.port.in;

import com.yapp.artie.domain.user.domain.User;

public interface GetUserQuery {

  User loadUserById(Long userId);
}
