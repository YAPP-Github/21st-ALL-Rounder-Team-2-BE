package com.yapp.artie.domain.user.adapter.out.authentication;

import com.google.firebase.auth.FirebaseToken;

public class ArtieToken {

  private final FirebaseToken firebaseToken;

  public ArtieToken(FirebaseToken firebaseToken) {
    this.firebaseToken = firebaseToken;
  }

  public String getUid() {
    return firebaseToken.getUid();
  }

  public String getName() {
    return firebaseToken.getName();
  }

  public String getPicture() {
    return firebaseToken.getPicture();
  }
}
