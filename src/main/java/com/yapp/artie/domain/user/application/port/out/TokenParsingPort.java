package com.yapp.artie.domain.user.application.port.out;

import com.yapp.artie.domain.user.domain.ArtieToken;

public interface TokenParsingPort {

  ArtieToken parseToken(String header);
}
