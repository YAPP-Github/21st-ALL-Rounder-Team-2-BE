package com.yapp.artie.domain.user.application.port.out;

import com.yapp.artie.domain.user.domain.User;
import java.time.LocalDateTime;

public interface LoadUserPort {

  User loadById(Long userId);

  User loadByUid(String uid);

  LocalDateTime loadJoinDateById(Long userId);
}
