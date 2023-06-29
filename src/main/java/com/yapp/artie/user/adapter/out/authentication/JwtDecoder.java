package com.yapp.artie.user.adapter.out.authentication;

import com.google.firebase.auth.AbstractFirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.yapp.artie.global.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class JwtDecoder {

  private final AbstractFirebaseAuth firebaseAuth;

  public FirebaseToken decode(String token) {
    try {
      return firebaseAuth.verifyIdToken(token);
    } catch (IllegalArgumentException e) {
      throw new NotExistValidTokenException();
    } catch (FirebaseAuthException e) {
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
