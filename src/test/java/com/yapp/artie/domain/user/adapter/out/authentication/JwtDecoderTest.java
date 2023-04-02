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
import com.yapp.artie.domain.user.adapter.out.authentication.exception.ExpiredTokenException;
import com.yapp.artie.domain.user.adapter.out.authentication.exception.NotExistValidTokenException;
import com.yapp.artie.domain.user.adapter.out.authentication.exception.RevokedTokenException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class JwtDecoderTest {

  private final AbstractFirebaseAuth firebaseAuth = Mockito.mock(AbstractFirebaseAuth.class);
  private final JwtDecoder jwtDecoder = new JwtDecoder(firebaseAuth);

  @Test
  void decode_firebase에_토큰_검증_요청을한다() throws Exception {
    jwtDecoder.decode("token");
    then(firebaseAuth)
        .should()
        .verifyIdToken(eq("token"));
  }

  @Test
  void decode_토큰이_null인_경우_예외를_발생한다() throws Exception {
    givenFirebaseAuthAcceptNull();
    assertThatThrownBy(() -> {
      jwtDecoder.decode(null);
    }).isInstanceOf(NotExistValidTokenException.class);
  }

  @Test
  void decode_토큰이_빈_문자열인_경우_예외를_발생한다() throws Exception {
    givenFirebaseAuthAcceptEmptyString();
    assertThatThrownBy(() -> {
      jwtDecoder.decode("");
    }).isInstanceOf(NotExistValidTokenException.class);
  }

  @Test
  void decode_토큰이_만료된_경우_예외를_발생한다() throws Exception {
    givenExpiredToken();
    assertThatThrownBy(() -> {
      jwtDecoder.decode("expired token");
    }).isInstanceOf(ExpiredTokenException.class);
  }

  @Test
  void decode_토큰이_취소된_경우_예외를_발생한다() throws Exception {
    givenRevokedToken();
    assertThatThrownBy(() -> {
      jwtDecoder.decode("revoked token");
    }).isInstanceOf(RevokedTokenException.class);
  }
  
  private void givenFirebaseAuthAcceptNull() throws Exception {
    doThrow(IllegalArgumentException.class)
        .when(firebaseAuth)
        .verifyIdToken(isNull());
  }

  private void givenFirebaseAuthAcceptEmptyString() throws Exception {
    doThrow(IllegalArgumentException.class)
        .when(firebaseAuth)
        .verifyIdToken(eq(""));
  }

  private void givenExpiredToken() throws Exception {
    doThrow(new FirebaseAuthException(
        ErrorCode.INVALID_ARGUMENT,
        "token is expired!",
        null,
        null,
        AuthErrorCode.EXPIRED_ID_TOKEN))
        .when(firebaseAuth)
        .verifyIdToken(any());
  }

  private void givenRevokedToken() throws Exception {
    doThrow(new FirebaseAuthException(
        ErrorCode.INVALID_ARGUMENT,
        "token has been revoked!",
        null,
        null,
        AuthErrorCode.REVOKED_ID_TOKEN))
        .when(firebaseAuth)
        .verifyIdToken(any());
  }
}