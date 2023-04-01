package com.yapp.artie.domain.user.service;

import com.yapp.artie.domain.user.adapter.out.persistence.UserJpaEntity;
import com.yapp.artie.domain.user.adapter.out.persistence.UserRepository;
import com.yapp.artie.domain.user.application.service.GetUserService;
import com.yapp.artie.domain.user.application.service.RegisterUserService;
import com.yapp.artie.domain.user.application.service.RenameUserService;
import com.yapp.artie.domain.user.application.service.UserWithdrawalService;
import com.yapp.artie.domain.user.domain.User;
import com.yapp.artie.domain.user.dto.response.CreateUserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

// TODO : deprecated api로 묶어서 JPA 엔티티를 반환하는 API만 레거시로 남겨둠
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserUseCase {

  private final RegisterUserService registerUserService;
  private final RenameUserService renameUserService;
  private final UserWithdrawalService userWithdrawalService;
  private final GetUserService getUserService;
  private final UserRepository userRepository;

  @Override
  public UserJpaEntity findById(Long id) {
    User user = getUserService.loadUserById(id);
    return new UserJpaEntity(user.getId(), user.getUid(), user.getProfileImage(), user.getName());
  }

  @Override
  public CreateUserResponseDto register(String uid, String username, String picture) {
    return registerUserService.register(uid, username, picture);
  }

  @Override
  public void delete(Long id) {
    userWithdrawalService.delete(id);
  }

  @Override
  public void updateUserName(Long userId, String name) {
    renameUserService.rename(userId, name);
  }
}
