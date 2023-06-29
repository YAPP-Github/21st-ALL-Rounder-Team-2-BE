package com.yapp.artie.user.application.port.in.query;

import com.yapp.artie.user.domain.User;

public interface GetUserQuery {

  User loadUserById(Long userId);
}
