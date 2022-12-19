package com.yapp.archiveServer.interceptor;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.yapp.archiveServer.global.exception.CustomException;
import com.yapp.archiveServer.global.exception.ErrorCode;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class FirebaseAuthInterceptor implements HandlerInterceptor {

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    String authType = "Bearer";
    String authHeader = Optional.ofNullable(request.getHeader("Authorization"))
        .orElseThrow(() -> new CustomException(ErrorCode.USER_TOKEN_ERROR, "헤더에 토큰이 존재하지 않습니다."));

    if (authHeader.startsWith(authType)) {
      authHeader = authHeader.substring(authType.length()).trim();
    }

    try {
      FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
      FirebaseToken decodedAuthToken = firebaseAuth.verifyIdToken(authHeader);
      log.info("[토큰 검증 성공] decodedAuthToken: {}", decodedAuthToken);
      String uid = decodedAuthToken.getUid();
      request.setAttribute("uid", uid);
      return true;
    } catch (IllegalArgumentException e) {
      // If the token is null, empty, or if the FirebaseApp instance does not have a project ID associated with it.
      throw new CustomException(ErrorCode.USER_TOKEN_ERROR, "유효한 토큰이 없습니다.");
    } catch (FirebaseAuthException e) {
      // If an error occurs while parsing or validating the token.
      throw new CustomException(ErrorCode.AUTH_SERVER_ERROR, "잘못된 토큰입니다.");
    }
  }
}