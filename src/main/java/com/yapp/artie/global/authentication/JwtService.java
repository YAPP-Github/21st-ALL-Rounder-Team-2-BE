package com.yapp.artie.global.authentication;

import com.google.firebase.auth.FirebaseToken;

public interface JwtService {

  FirebaseToken verify(String header);

  void withdraw(String uid);
}
