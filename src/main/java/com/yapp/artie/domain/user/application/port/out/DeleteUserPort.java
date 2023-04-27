package com.yapp.artie.domain.user.application.port.out;

import com.yapp.artie.domain.user.domain.User;

public interface DeleteUserPort {

  void delete(User user);
}
