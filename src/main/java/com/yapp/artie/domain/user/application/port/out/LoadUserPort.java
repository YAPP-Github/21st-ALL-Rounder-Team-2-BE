package com.yapp.artie.domain.user.application.port.out;

import com.yapp.artie.domain.user.domain.User;

public interface LoadUserPort {

  User loadById(Long userId);

  User loadByUid(String uid);
}
