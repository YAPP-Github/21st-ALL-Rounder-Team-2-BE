package com.yapp.artie.domain.user.adapter.out.authentication;

import com.yapp.artie.domain.user.application.port.out.JwtService;
import com.yapp.artie.global.exception.authentication.NotExistValidTokenException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

// TODO : 이름 변경, 애노테이션 생성
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

  private final WithdrawalHandler withdrawalHandler;
  private final JwtDecoder decoder;

  @Override
  public ArtieToken verify(String header) {
    validateHeader(header);
    return new ArtieToken(decoder
        .decode(refineHeaderAsToken(header)));
  }

  @Override
  public void withdraw(String uid) {
    withdrawalHandler.withdraw(uid);
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
