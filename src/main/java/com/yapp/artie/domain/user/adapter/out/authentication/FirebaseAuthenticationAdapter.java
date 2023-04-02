package com.yapp.artie.domain.user.adapter.out.authentication;

import com.yapp.artie.domain.user.adapter.out.authentication.exception.NotExistValidTokenException;
import com.yapp.artie.domain.user.application.port.out.DeleteExternalUserPort;
import com.yapp.artie.domain.user.application.port.out.TokenParsingPort;
import com.yapp.artie.domain.user.domain.ArtieToken;
import com.yapp.artie.global.annotation.AuthenticationAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AuthenticationAdapter
@RequiredArgsConstructor
public class FirebaseAuthenticationAdapter implements DeleteExternalUserPort, TokenParsingPort {

  private final FirebaseUserRemover firebaseUserRemover;
  private final JwtDecoder decoder;
  private final TokenGenerator tokenGenerator;

  @Override
  public ArtieToken parseToken(String header) {
    validateHeader(header);
    return tokenGenerator.generateDomainToken(decoder
        .decode(refineHeaderAsToken(header)));
  }

  @Override
  public void delete(String uid) {
    firebaseUserRemover.remove(uid);
  }

  private void validateHeader(String header) {
    if (header == null || !header.startsWith("Bearer ") || header.trim().equals("Bearer")) {
      throw new NotExistValidTokenException();
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
