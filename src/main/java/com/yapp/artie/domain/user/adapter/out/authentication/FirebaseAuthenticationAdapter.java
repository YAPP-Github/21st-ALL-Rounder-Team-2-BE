package com.yapp.artie.domain.user.adapter.out.authentication;

import com.yapp.artie.domain.user.application.port.out.DeleteExternalUserPort;
import com.yapp.artie.domain.user.application.port.out.GenerateTestTokenPort;
import com.yapp.artie.domain.user.application.port.out.TokenParsingPort;
import com.yapp.artie.domain.user.domain.ArtieToken;
import com.yapp.artie.global.common.annotation.AuthenticationAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AuthenticationAdapter
@RequiredArgsConstructor
class FirebaseAuthenticationAdapter implements DeleteExternalUserPort, TokenParsingPort,
    GenerateTestTokenPort {

  private final FirebaseUserRemover firebaseUserRemover;
  private final JwtDecoder decoder;
  private final TokenGenerator tokenGenerator;
  private final FirebaseTokenGenerator firebaseTokenGenerator;

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

  @Override
  public String generateTestToken(String uid) {
    return firebaseTokenGenerator.generate(uid);
  }
}
