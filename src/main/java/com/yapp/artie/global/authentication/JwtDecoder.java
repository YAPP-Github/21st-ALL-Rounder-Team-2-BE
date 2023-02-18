package com.yapp.artie.global.authentication;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.yapp.artie.global.exception.authentication.ExpiredTokenException;
import com.yapp.artie.global.exception.authentication.InvalidTokenException;
import com.yapp.artie.global.exception.authentication.NotExistValidTokenException;
import com.yapp.artie.global.exception.authentication.RevokedTokenException;
import com.yapp.artie.global.exception.common.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtDecoder {

  private final FirebaseAuth firebaseAuth;

  public FirebaseToken decode(String token) {
    try {
      return firebaseAuth.verifyIdToken(token);
    } catch (IllegalArgumentException e) {
      // If the token is null, empty,
      // or if the FirebaseApp instance does not have a project ID associated with it.
      throw new NotExistValidTokenException();
    } catch (FirebaseAuthException e) {
      // If an error occurs while parsing or validating the token.
      throw processAuthException(e);
    }
  }

  private BusinessException processAuthException(FirebaseAuthException e) {
    switch (e.getAuthErrorCode()) {
      case EXPIRED_ID_TOKEN:
        return new ExpiredTokenException();
      case REVOKED_ID_TOKEN:
        return new RevokedTokenException();
      default:
        return new InvalidTokenException();
    }
  }

}
