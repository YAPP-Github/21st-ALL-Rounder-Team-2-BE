package com.yapp.artie.user.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.yapp.artie.user.domain.User;
import com.yapp.artie.user.repository.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  public Optional<User> findByUid(String uid) {
    return userRepository.findByUid(uid);
  }

  public User createUser(String uid) throws FirebaseAuthException {
    // firebaseAuth를 메소드 변수가 아닌 서비스 변수로 변경 필요
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    String name = firebaseAuth.getUser(uid).getDisplayName();
    String profileImage = firebaseAuth.getUser(uid).getPhotoUrl();
    User newUser = new User();
    newUser.setUid(uid);
    if (name != null) {
      newUser.setName(name);
    }
    if (profileImage != null) {
      newUser.setProfileImage(profileImage);
    }
    return userRepository.save(newUser);
  }
}
