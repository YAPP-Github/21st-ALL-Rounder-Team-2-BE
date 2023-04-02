package com.yapp.artie.domain.user.adapter.out.authentication;

import com.google.firebase.auth.AuthErrorCode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.yapp.artie.global.exception.authentication.FirebaseUserNotFoundException;
import com.yapp.artie.global.exception.authentication.InvalidFirebaseUidException;
import com.yapp.artie.global.exception.common.BusinessException;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FirebaseUserRemover {

  private final FirebaseAuth firebaseAuth;

  public void remove(String uid) {
    try {
      firebaseAuth.deleteUser(uid);
    } catch (IllegalArgumentException e) {
      throw new InvalidFirebaseUidException();
    } catch (FirebaseAuthException e) {
      throw processRemoveException(e);
    }
  }

  private BusinessException processRemoveException(FirebaseAuthException e) {
    if (Objects.requireNonNull(e.getAuthErrorCode()) == AuthErrorCode.USER_NOT_FOUND) {
      return new FirebaseUserNotFoundException();
    }

    return new InvalidFirebaseUidException();
  }
}

