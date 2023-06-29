package com.yapp.artie.user.application.port.out;

import com.yapp.artie.user.domain.ArtieToken;

public interface TokenParsingPort {

  ArtieToken parseToken(String header);
}
