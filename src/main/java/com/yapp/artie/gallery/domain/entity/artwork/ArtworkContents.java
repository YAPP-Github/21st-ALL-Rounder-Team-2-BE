package com.yapp.artie.gallery.domain.entity.artwork;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArtworkContents {

  @Column
  private String name;

  @Column
  private String artist;

  @Column(nullable = false)
  private String uri;

  private ArtworkContents(Builder builder) {
    name = builder.name;
    artist = builder.artist;
    uri = builder.uri;
  }

  public static class Builder {

    // 필수 매개변수
    private final String uri;

    // 선택 매개변수
    private String name;
    private String artist;

    // 필수 매개변수만을 담은 Builder 생성자
    public Builder(String uri) {
      this.uri = uri;
    }

    // 선택 매개변수의 setter, Builder 자신을 반환해 연쇄적으로 호출 가능
    public Builder name(String val) {
      this.name = val;
      return this;
    }

    public Builder artist(String val) {
      this.artist = val;
      return this;
    }

    // build() 호출로 최종 불변 객체를 얻는다.
    public ArtworkContents build() {
      return new ArtworkContents(this);
    }
  }

  public void updateArtist(String artist) {
    this.artist = artist;
  }

  public void updateName(String name) {
    this.name = name;
  }
}
