package com.yapp.artie.domain.user.adapter.out.authentication;

import com.google.firebase.auth.FirebaseToken;
import com.yapp.artie.domain.user.domain.ArtieToken;
import org.springframework.stereotype.Component;

@Component
public class TokenGenerator {

  ArtieToken generateDomainToken(FirebaseToken firebaseToken) {
    return new ArtieToken(firebaseToken.getUid(), firebaseToken.getName(),
        firebaseToken.getPicture());
  }
}
