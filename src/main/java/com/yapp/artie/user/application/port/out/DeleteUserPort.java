package com.yapp.artie.user.application.port.out;

import com.yapp.artie.user.domain.User;

public interface DeleteUserPort {

  void delete(User user);
}
