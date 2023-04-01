package com.yapp.artie.domain.user.application.port.out;

import com.yapp.artie.domain.user.adapter.out.authentication.ArtieToken;

public interface JwtService {

  ArtieToken verify(String header);

  void withdraw(String uid);
}
