package com.yapp.artie.domain.user.adapter.in.web;

import static com.yapp.artie.common.UserTestData.defaultUser;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.yapp.artie.common.BaseControllerIntegrationTest;
import com.yapp.artie.domain.user.application.port.in.GetUserThumbnailQuery;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@WebMvcTest(controllers = GetUserThumbnailController.class)
class GetUserThumbnailControllerTest extends BaseControllerIntegrationTest {

  @MockBean
  private GetUserThumbnailQuery getUserThumbnailQuery;

  @Test
  void testMy() throws Exception {
    givenUserByReference(defaultUser()
        .withName("tomcat")
        .build());

    mvc.perform(get("/user/my-page")
        .header("Content-Type", "application/json")
        .header("Authorization", "sample")
    ).andExpect(status().isOk());

    then(getUserThumbnailQuery).should()
        .loadUserThumbnailById(eq(1L));
  }
}