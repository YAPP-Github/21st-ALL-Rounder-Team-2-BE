package com.yapp.artie.domain.user.adapter.out.persistence;

import com.yapp.artie.domain.user.application.port.out.DeleteUserPort;
import com.yapp.artie.domain.user.application.port.out.LoadUserPort;
import com.yapp.artie.domain.user.application.port.out.SaveUserPort;
import com.yapp.artie.domain.user.application.port.out.UpdateUserStatePort;
import com.yapp.artie.domain.user.domain.User;
import com.yapp.artie.domain.user.domain.UserNotFoundException;
import com.yapp.artie.global.common.annotation.PersistenceAdapter;
import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
class UserPersistenceAdapter implements DeleteUserPort, SaveUserPort, LoadUserPort,
    UpdateUserStatePort {

  private final UserRepository userRepository;
  private final UserMapper userMapper;

  @Override
  public User loadById(Long userId) {
    return userMapper.mapToDomainEntity(findByIdOrElseThrow(userId));
  }

  @Override
  public User loadByUid(String uid) {
    return userMapper.mapToDomainEntity(findByUidOrElseThrow(uid));
  }

  @Override
  public Long save(User user) {
    UserJpaEntity entity = userRepository.save(userMapper.mapToJpaEntity(user));
    return entity.getId();
  }

  @Override
  public void delete(User user) {
    userRepository.delete(findByIdOrElseThrow(user.getId()));
  }

  @Override
  public void updateName(User user) {
    UserJpaEntity userJpaEntity = findByIdOrElseThrow(user.getId());
    userJpaEntity.setName(user.getName());
  }

  private UserJpaEntity findByUidOrElseThrow(String uid) {
    return userRepository.findByUid(uid)
        .orElseThrow(UserNotFoundException::new);
  }

  private UserJpaEntity findByIdOrElseThrow(Long id) {
    return userRepository.findById(id)
        .orElseThrow(UserNotFoundException::new);
  }
}
