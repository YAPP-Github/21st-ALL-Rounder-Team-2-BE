package com.yapp.artie.domain.user.application.port.in;

public interface GetTestUserTokenQuery {

  String loadTestUserToken(Long userId);
}
