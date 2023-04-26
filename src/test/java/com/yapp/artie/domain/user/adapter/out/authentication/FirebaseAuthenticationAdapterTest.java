package com.yapp.artie.domain.user.adapter.out.authentication;

import static com.yapp.artie.common.UserTestData.defaultUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.yapp.artie.domain.user.domain.ArtieToken;
import com.yapp.artie.domain.user.domain.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

class FirebaseAuthenticationAdapterTest {

  private final JwtDecoder jwtDecoder = Mockito.mock(JwtDecoder.class);
  private final FirebaseUserRemover firebaseUserRemover = Mockito.mock(FirebaseUserRemover.class);
  private final TokenGenerator tokenGenerator = Mockito.mock(TokenGenerator.class);
  private final FirebaseTokenGenerator firebaseTokenGenerator = Mockito.mock(
      FirebaseTokenGenerator.class);
  private final FirebaseAuthenticationAdapter adapterUnderTest =
      new FirebaseAuthenticationAdapter(firebaseUserRemover, jwtDecoder, tokenGenerator,
          firebaseTokenGenerator);

  @Test
  void parseToken_헤더가_null인_경우_예외를_발생한다() {
    assertThatThrownBy(() -> {
      adapterUnderTest.parseToken(null);
    }).isInstanceOf(NotExistValidTokenException.class);
  }

  @Test
  void parseToken_헤더가_Bearer_로_시작하지_않는_경우_예외를_발생한다() {
    assertThatThrownBy(() -> {
      adapterUnderTest.parseToken("Drink Beer");
    }).isInstanceOf(NotExistValidTokenException.class);
  }

  @ParameterizedTest(name = "[{index}] Authorization 헤더가 {0}인 경우")
  @ValueSource(strings = {"Bearer", "Bearer ", " Bearer", "  Bearer", "Bearer  "})
  void parseToken_헤더가_Bearer만_포함하는_경우_예외를_발생한다(String header) {
    assertThatThrownBy(() -> {
      adapterUnderTest.parseToken(header);
    }).isInstanceOf(NotExistValidTokenException.class);
  }

  @Test
  void parseToken_유효한_Bearer_토큰이라면_새로운_사용자_인증_토큰을_반환한다() {
    User user = defaultUser()
        .withUid("uid")
        .withName("tomcat")
        .withProfileImage("sample.com")
        .build();

    givenArtieTokenByReference(user);
    ArtieToken actual = adapterUnderTest.parseToken("Bearer valid token");

    assertThat(actual.getUid()).isEqualTo("uid");
    assertThat(actual.getName()).isEqualTo("tomcat");
    assertThat(actual.getPicture()).isEqualTo("sample.com");
  }

  @Test
  public void parseToken_토큰이_들어오면_토큰의_타입을_제거해서_decoder에게_전달한다() throws Exception {
    String header = "Bearer tomcat";
    adapterUnderTest.parseToken(header);
    then(jwtDecoder)
        .should()
        .decode(eq("tomcat"));
  }

  @Test
  public void delete_Remover에게_삭제_요청을_한다() {
    adapterUnderTest.delete("uid");
    then(firebaseUserRemover)
        .should()
        .remove(eq("uid"));
  }

  @Test
  public void generateTestToken_주어진_uid를_가지는_테스트유저의_FirebaseCustomToken발급을_요청한다() {
    adapterUnderTest.generateTestToken("uid");
    then(firebaseTokenGenerator).should().generate("uid");
  }

  private void givenArtieTokenByReference(User user) {
    given(tokenGenerator.generateDomainToken(any()))
        .willReturn(new ArtieToken(user.getUid(), user.getName(), user.getProfileImage()));
  }
}