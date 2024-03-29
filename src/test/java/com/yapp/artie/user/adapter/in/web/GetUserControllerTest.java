package com.yapp.artie.user.adapter.in.web;

import static com.yapp.artie.common.UserTestData.defaultUser;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.yapp.artie.common.BaseControllerIntegrationTest;
import com.yapp.artie.user.application.port.in.query.GetUserQuery;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@WebMvcTest(controllers = GetUserController.class)
class GetUserControllerTest extends BaseControllerIntegrationTest {

  @MockBean
  private GetUserQuery getUserQuery;

  @Test
  void testMe() throws Exception {
    givenUserByReference(defaultUser().withId(1L).build());

    mvc.perform(get("/user/me")
            .header("Content-Type", "application/json")
        )
        .andExpect(status().isOk());

    then(getUserQuery).should()
        .loadUserById(eq(1L));
  }
}