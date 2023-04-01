package com.yapp.artie.common;

import com.yapp.artie.domain.user.adapter.out.authentication.ArtieToken;

public class TestArtieToken extends ArtieToken {

  private final String uid;
  private final String name;
  private final String picture;

  public TestArtieToken(String uid, String name,
      String picture) {
    super(null);
    this.uid = uid;
    this.name = name;
    this.picture = picture;
  }

  @Override
  public String getUid() {
    return uid;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getPicture() {
    return picture;
  }
}
