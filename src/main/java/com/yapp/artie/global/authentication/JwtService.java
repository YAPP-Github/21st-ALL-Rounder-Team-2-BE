package com.yapp.artie.global.authentication;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.yapp.artie.global.error.exception.authentication.InvalidTokenException;
import com.yapp.artie.global.error.exception.authentication.NotExistValidTokenException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtService {

  private final FirebaseAuth firebaseAuth;

  public FirebaseToken verify(String header) {
    validateHeader(header);
    return decode(refineHeaderAsToken(header));
  }

  private void validateHeader(String header) {
    if (header == null || !header.startsWith("Bearer ")) {
      throw new NotExistValidTokenException();
    }
  }

  private FirebaseToken decode(String token) {
    try {
      return firebaseAuth.verifyIdToken(token);
    } catch (IllegalArgumentException e) {
      // If the token is null, empty,
      // or if the FirebaseApp instance does not have a project ID associated with it.
      throw new NotExistValidTokenException();
    } catch (FirebaseAuthException e) {
      // If an error occurs while parsing or validating the token.
      throw new InvalidTokenException();
    }
  }

  private String refineHeaderAsToken(String header) {
    String authType = "Bearer";
    if (header.startsWith(authType)) {
      header = header.substring(authType.length()).trim();
    }
    return header;
  }
}
