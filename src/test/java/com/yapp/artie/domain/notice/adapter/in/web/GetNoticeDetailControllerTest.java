package com.yapp.artie.domain.notice.adapter.in.web;

import static com.yapp.artie.common.UserTestData.defaultUser;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.yapp.artie.common.BaseControllerIntegrationTest;
import com.yapp.artie.domain.notice.application.port.in.GetNoticeDetailQuery;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

@WebMvcTest(controllers = GetNoticeDetailController.class)
class GetNoticeDetailControllerTest extends BaseControllerIntegrationTest {

  @MockBean
  private GetNoticeDetailQuery getNoticeDetailQuery;

  @Test
  void getNoticeDetail() throws Exception {
    givenUserByReference(defaultUser().build());
    Long noticeId = 1L;

    mvc.perform(get("/notice/{noticeId}", noticeId).accept(MediaType.APPLICATION_JSON)
            .header("Authorization", "sample"))
        .andExpect(status().isOk());

    then(getNoticeDetailQuery).should().loadNoticeDetail(noticeId);
  }
}