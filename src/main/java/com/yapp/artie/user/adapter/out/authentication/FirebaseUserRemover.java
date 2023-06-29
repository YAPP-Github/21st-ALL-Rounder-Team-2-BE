package com.yapp.artie.user.adapter.out.authentication;

import com.google.firebase.auth.AbstractFirebaseAuth;
import com.google.firebase.auth.AuthErrorCode;
import com.google.firebase.auth.FirebaseAuthException;
import com.yapp.artie.global.common.exception.BusinessException;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class FirebaseUserRemover {

  private final AbstractFirebaseAuth firebaseAuth;

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

