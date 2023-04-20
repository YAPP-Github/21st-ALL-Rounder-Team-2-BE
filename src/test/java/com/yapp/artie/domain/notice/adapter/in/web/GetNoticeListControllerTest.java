package com.yapp.artie.domain.notice.adapter.in.web;

import static com.yapp.artie.common.UserTestData.defaultUser;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.yapp.artie.common.BaseControllerIntegrationTest;
import com.yapp.artie.domain.notice.application.port.in.GetNoticeListQuery;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

@WebMvcTest(controllers = GetNoticeListController.class)
class GetNoticeListControllerTest extends BaseControllerIntegrationTest {

  @MockBean
  private GetNoticeListQuery getNoticeListQuery;

  @Test
  void getNotices() throws Exception {
    givenUserByReference(defaultUser().build());

    mvc.perform(get("/notice").accept(MediaType.APPLICATION_JSON)
            .header("Authorization", "sample"))
        .andExpect(status().isOk());

    then(getNoticeListQuery).should().loadNoticeList();
  }
}