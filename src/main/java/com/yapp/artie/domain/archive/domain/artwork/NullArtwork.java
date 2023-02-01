package com.yapp.artie.domain.archive.domain.artwork;

// TODO : null object, 현재 구현 상속(extends)한 상황, 타입 상속(implements)으로 변경 필요
// [null object pattern(special case)](https://martinfowler.com/eaaCatalog/specialCase.html)
public class NullArtwork extends Artwork {

  private NullArtwork() {
    super(null, false, new ArtworkContents
        .Builder(null)
        .name(null)
        .artist(null)
        .build());
  }

  @Override
  public Long getId() {
    return 0L;
  }

  public static Artwork create() {
    return new NullArtwork();
  }
}
