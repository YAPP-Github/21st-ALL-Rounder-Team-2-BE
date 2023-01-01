package com.yapp.artie.global.interceptor;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.yapp.artie.global.error.exception.ErrorCode;
import com.yapp.artie.global.error.exception.InvalidValueException;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class FirebaseAuthInterceptor implements HandlerInterceptor {

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
      Object handler) {

    String firebaseIdToken = getFirebaseIdToken(getAuthorizationHeader(request));

    try {
      verify(request, firebaseIdToken);
    } catch (IllegalArgumentException e) {
      // If the token is null, empty, or if the FirebaseApp instance does not have a project ID associated with it.
      throw new NotExistValidTokenException();
    } catch (FirebaseAuthException e) {
      // If an error occurs while parsing or validating the token.
      throw new InvalidTokenException();
    }

    return true;
  }

  private String getAuthorizationHeader(HttpServletRequest request) {
    return Optional.ofNullable(request.getHeader("Authorization"))
        .orElseThrow(NotExistValidTokenException::new);
  }

  private String getFirebaseIdToken(String authHeader) {
    String authType = "Bearer";
    if (authHeader.startsWith(authType)) {
      authHeader = authHeader.substring(authType.length()).trim();
    }

    return authHeader;
  }

  private void verify(HttpServletRequest request, String firebaseIdToken)
      throws FirebaseAuthException {
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseToken decodedAuthToken = firebaseAuth.verifyIdToken(firebaseIdToken);

    log.info("[토큰 검증 성공] decodedAuthToken: {}", decodedAuthToken);
    request.setAttribute("uid", decodedAuthToken.getUid());
  }

  static class InvalidTokenException extends InvalidValueException {

    public InvalidTokenException() {
      super(ErrorCode.USER_TOKEN_ERROR.getMessage(), ErrorCode.USER_TOKEN_ERROR);
    }
  }

  static class NotExistValidTokenException extends InvalidValueException {

    public NotExistValidTokenException() {
      super(ErrorCode.AUTH_TOKEN_NOT_EXISTS.getMessage(), ErrorCode.AUTH_TOKEN_NOT_EXISTS);
    }
  }
}