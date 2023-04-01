package com.yapp.artie.domain.user.adapter.in.web;

import static com.yapp.artie.common.UserTestData.defaultUser;
import static org.mockito.BDDMockito.eq;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.yapp.artie.common.BaseControllerIntegrationTest;
import com.yapp.artie.domain.user.application.port.in.RegisterUserUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@WebMvcTest(controllers = RegisterUserController.class)
class RegisterUserControllerTest extends BaseControllerIntegrationTest {

  @MockBean
  private RegisterUserUseCase registerUserUseCase;

  @Test
  void testRegister() throws Exception {
    givenUserByReference(defaultUser()
        .withName("tomcat")
        .withProfileImage("test.com")
        .build());

    mvc.perform(post("/user")
        .header("Content-Type", "application/json")
        .header("Authorization", "sample")
        .param("uid", "uid")
    ).andExpect(status().isCreated());

    then(registerUserUseCase).should()
        .register(eq("uid"), eq("tomcat"), eq("test.com"));
  }
}