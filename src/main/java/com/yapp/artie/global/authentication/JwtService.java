package com.yapp.artie.global.authentication;

public interface JwtService {

  ArtieToken verify(String header);

  void withdraw(String uid);
}
