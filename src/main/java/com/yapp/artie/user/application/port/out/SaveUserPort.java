package com.yapp.artie.user.application.port.out;

import com.yapp.artie.user.domain.User;

public interface SaveUserPort {

  Long save(User user);
}
