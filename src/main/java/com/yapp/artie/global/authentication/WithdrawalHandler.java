package com.yapp.artie.global.authentication;

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
public class WithdrawalHandler {

  private final FirebaseAuth firebaseAuth;

  public void withdraw(String uid) {
    try {
      firebaseAuth.deleteUser(uid);
    } catch (IllegalArgumentException e) {
      throw new InvalidFirebaseUidException();
    } catch (FirebaseAuthException e) {
      throw processWithDrawException(e);
    }
  }

  private BusinessException processWithDrawException(FirebaseAuthException e) {
    if (Objects.requireNonNull(e.getAuthErrorCode()) == AuthErrorCode.USER_NOT_FOUND) {
      return new FirebaseUserNotFoundException();
    }

    return new InvalidFirebaseUidException();
  }
}

