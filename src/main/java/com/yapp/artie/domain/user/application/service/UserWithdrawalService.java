package com.yapp.artie.domain.user.application.service;

import com.yapp.artie.domain.archive.repository.CategoryRepository;
import com.yapp.artie.domain.user.adapter.out.persistence.UserJpaEntity;
import com.yapp.artie.domain.user.application.port.in.UserWithdrawalUseCase;
import com.yapp.artie.domain.user.application.port.out.DeleteExternalUserPort;
import com.yapp.artie.domain.user.application.port.out.DeleteUserPort;
import com.yapp.artie.domain.user.application.port.out.LoadUserPort;
import com.yapp.artie.domain.user.domain.User;
import com.yapp.artie.global.common.annotation.UseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@Transactional
@RequiredArgsConstructor
public class UserWithdrawalService implements UserWithdrawalUseCase {

  private final DeleteExternalUserPort deleteExternalUserPort;
  private final CategoryRepository categoryRepository;
  private final LoadUserPort loadUserPort;
  private final DeleteUserPort deleteUserPort;

  @Override
  public void delete(Long id) {
    User user = loadUserPort.loadById(id);
    deleteExternalUserPort.delete(user.getUid());
    // TODO : 꼭 변경해야함! User 도메인을 받던지 따로 입력 모델을 만들자.
    categoryRepository.deleteAllByUser(
        new UserJpaEntity(user.getId(), user.getUid(), user.getProfileImage(), user.getName()));
    deleteUserPort.delete(user);
  }
}