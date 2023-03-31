package com.yapp.artie.common;

import com.yapp.artie.global.authentication.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest
@MockBean(JpaMetamodelMappingContext.class)
public abstract class BaseControllerIntegrationTest {

  @Autowired
  protected MockMvc mvc;

  @MockBean
  protected JwtService jwtService;
}