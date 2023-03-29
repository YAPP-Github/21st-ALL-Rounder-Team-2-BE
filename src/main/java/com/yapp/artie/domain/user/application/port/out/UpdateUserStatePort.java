package com.yapp.artie.domain.user.application.port.out;

import com.yapp.artie.domain.user.domain.User;

public interface UpdateUserStatePort {

  void updateName(User user);
}
