package com.yapp.artie.common;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.core.userdetails.User.builder;

import com.yapp.artie.domain.user.application.port.in.GetUserQuery;
import com.yapp.artie.domain.user.application.port.out.JwtService;
import com.yapp.artie.domain.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest
@MockBean(JpaMetamodelMappingContext.class)
public abstract class BaseControllerIntegrationTest {

  @Autowired
  protected MockMvc mvc;

  @MockBean
  protected JwtService jwtService;

  @MockBean
  protected UserDetailsService userDetailsService;

  protected void givenUserByReference(User user) {
    loadUserDetailsWillReturnReference(user);
    givenTestToken(user.getUid(), user.getName(), user.getProfileImage());
  }

  private void loadUserDetailsWillReturnReference(User user) {
    given(userDetailsService.loadUserByUsername(any()))
        .willReturn(builder()
            .username(String.valueOf(user.getId()))
            .password(user.getUid())
            .authorities("user")
            .build());
  }

  private void givenTestToken(String uid, String name, String picture) {
    given(jwtService.verify(any()))
        .willReturn(new TestArtieToken(uid, name, picture));
  }
}