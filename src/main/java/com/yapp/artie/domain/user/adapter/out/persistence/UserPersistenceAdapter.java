package com.yapp.artie.domain.user.adapter.out.persistence;

import com.yapp.artie.domain.user.application.port.out.DeleteUserPort;
import com.yapp.artie.domain.user.application.port.out.LoadUserPort;
import com.yapp.artie.domain.user.application.port.out.SaveUserPort;
import com.yapp.artie.domain.user.application.port.out.UpdateUserStatePort;
import com.yapp.artie.domain.user.domain.User;
import com.yapp.artie.global.annotation.PersistenceAdapter;
import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
class UserPersistenceAdapter implements DeleteUserPort, SaveUserPort, LoadUserPort,
    UpdateUserStatePort {

  private final UserRepository userRepository;
  private final UserMapper userMapper;

  @Override
  public User loadById(Long userId) {
    return null;
  }

  @Override
  public User loadByUid(String uid) {
    return null;
  }

  @Override
  public void delete(User user) {

  }

  @Override
  public void save(User user) {

  }

  @Override
  public void updateName(User user) {

  }
}
