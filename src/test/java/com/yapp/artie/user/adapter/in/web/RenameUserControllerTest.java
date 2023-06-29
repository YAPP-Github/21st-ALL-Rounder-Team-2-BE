package com.yapp.artie.user.adapter.in.web;

import static com.yapp.artie.common.UserTestData.defaultUser;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.yapp.artie.common.BaseControllerIntegrationTest;
import com.yapp.artie.user.application.port.in.command.RenameUserUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@WebMvcTest(controllers = RenameUserController.class)
class RenameUserControllerTest extends BaseControllerIntegrationTest {

  @MockBean
  private RenameUserUseCase renameUserUseCase;

  @Test
  void testRename() throws Exception {
    givenUserByReference(defaultUser().withId(1L).build());

    mvc.perform(patch("/user")
            .header("Content-Type", "application/json")
            .queryParam("name", "tomcat")
        )
        .andExpect(status().isNoContent());

    then(renameUserUseCase).should()
        .rename(eq(1L), eq("tomcat"));
  }
}