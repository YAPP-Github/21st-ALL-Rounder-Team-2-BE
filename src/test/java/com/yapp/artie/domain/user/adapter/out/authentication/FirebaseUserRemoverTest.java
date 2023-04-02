package com.yapp.artie.domain.user.adapter.out.authentication;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doThrow;

import com.google.firebase.ErrorCode;
import com.google.firebase.auth.AbstractFirebaseAuth;
import com.google.firebase.auth.AuthErrorCode;
import com.google.firebase.auth.FirebaseAuthException;
import com.yapp.artie.domain.user.adapter.out.authentication.exception.FirebaseUserNotFoundException;
import com.yapp.artie.domain.user.adapter.out.authentication.exception.InvalidFirebaseUidException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class FirebaseUserRemoverTest {

  private final AbstractFirebaseAuth firebaseAuth = Mockito.mock(AbstractFirebaseAuth.class);
  private final FirebaseUserRemover firebaseUserRemover = new FirebaseUserRemover(firebaseAuth);

  @Test
  void remove_firebase에_삭제요청을한다() throws Exception {
    firebaseUserRemover.remove("uid");
    then(firebaseAuth)
        .should()
        .deleteUser(eq("uid"));
  }

  @Test
  void remove_uid가_null인_경우_예외를_발생한다() throws Exception {
    givenFirebaseAuthAcceptNull();
    assertThatThrownBy(() -> {
      firebaseUserRemover.remove(null);
    }).isInstanceOf(InvalidFirebaseUidException.class);
  }

  @Test
  void remove_uid가_빈_문자열인_경우_예외를_발생한다() throws Exception {
    givenFirebaseAuthAcceptEmptyString();
    assertThatThrownBy(() -> {
      firebaseUserRemover.remove("");
    }).isInstanceOf(InvalidFirebaseUidException.class);
  }

  @Test
  void remove_존재하지_않는_사용자라면_예외를_발생한다() throws Exception {
    givenUserFindWillFail();
    assertThatThrownBy(() -> {
      firebaseUserRemover.remove("");
    }).isInstanceOf(FirebaseUserNotFoundException.class);
  }

  private void givenFirebaseAuthAcceptNull() throws Exception {
    doThrow(IllegalArgumentException.class)
        .when(firebaseAuth)
        .deleteUser(isNull());
  }

  private void givenFirebaseAuthAcceptEmptyString() throws Exception {
    doThrow(IllegalArgumentException.class)
        .when(firebaseAuth)
        .deleteUser(eq(""));
  }

  private void givenUserFindWillFail() throws Exception {
    doThrow(new FirebaseAuthException(
        ErrorCode.NOT_FOUND,
        "user not found",
        null,
        null,
        AuthErrorCode.USER_NOT_FOUND))
        .when(firebaseAuth)
        .deleteUser(any());
  }
}