package com.yapp.artie.user.adapter.in.web;

import static com.yapp.artie.common.UserTestData.defaultUser;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.yapp.artie.common.BaseControllerIntegrationTest;
import com.yapp.artie.user.application.port.in.command.UserWithdrawalUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@WebMvcTest(controllers = UserWithdrawalController.class)
class UserWithdrawalControllerTest extends BaseControllerIntegrationTest {

  @MockBean
  private UserWithdrawalUseCase userWithdrawalUseCase;

  @Test
  void testDelete() throws Exception {
    givenUserByReference(defaultUser().withId(1L).build());

    mvc.perform(delete("/user")
            .header("Content-Type", "application/json")
        )
        .andExpect(status().isNoContent());

    then(userWithdrawalUseCase).should()
        .delete(eq(1L));
  }
}