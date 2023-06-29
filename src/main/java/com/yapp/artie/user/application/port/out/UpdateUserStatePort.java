package com.yapp.artie.user.application.port.out;

import com.yapp.artie.user.domain.User;

public interface UpdateUserStatePort {

  void updateName(User user);
}
