package com.yapp.artie.domain.user.application.port.in.query;

public interface GetTestUserTokenQuery {

  String loadTestUserToken(Long userId);
}
