package com.yapp.artie.user.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ArtieToken {

  private final String uid;
  private final String name;
  private final String picture;
}
