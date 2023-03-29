package com.yapp.artie.domain.user.adapter.out.persistence;

import com.yapp.artie.domain.user.application.port.out.DeleteUserPort;
import com.yapp.artie.domain.user.application.port.out.LoadUserPort;
import com.yapp.artie.domain.user.application.port.out.SaveUserPort;
import com.yapp.artie.domain.user.application.port.out.UpdateUserStatePort;
import com.yapp.artie.domain.user.domain.User;
import com.yapp.artie.domain.user.exception.UserNotFoundException;
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
    return userMapper.mapToDomainEntity(findByIdOrThrow(userId));
  }

  @Override
  public User loadByUid(String uid) {
    UserJpaEntity userJpaEntity = userRepository.findByUid(uid)
        .orElseThrow(UserNotFoundException::new);

    return userMapper.mapToDomainEntity(userJpaEntity);
  }

  @Override
  public void save(User user) {
    userRepository.save(userMapper.mapToJpaEntity(user));
  }

  @Override
  public void delete(User user) {
    userRepository.delete(findByIdOrThrow(user.getId()));
  }

  @Override
  public void updateName(User user) {
    UserJpaEntity userJpaEntity = findByIdOrThrow(user.getId());
    userJpaEntity.setName(user.getName());
  }

  private UserJpaEntity findByIdOrThrow(Long id) {
    return userRepository.findById(id)
        .orElseThrow(UserNotFoundException::new);
  }
}
