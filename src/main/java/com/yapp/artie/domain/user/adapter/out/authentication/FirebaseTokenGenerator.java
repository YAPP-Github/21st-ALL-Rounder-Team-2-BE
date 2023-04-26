package com.yapp.artie.domain.user.adapter.out.authentication;

import com.google.firebase.auth.AbstractFirebaseAuth;
import com.google.firebase.auth.AuthErrorCode;
import com.google.firebase.auth.FirebaseAuthException;
import com.yapp.artie.global.common.exception.BusinessException;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class FirebaseTokenGenerator {

  private final AbstractFirebaseAuth firebaseAuth;

  public String generate(String uid) {
    try {
      return firebaseAuth.createCustomToken(uid);
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

