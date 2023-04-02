package com.yapp.artie.global.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yapp.artie.global.common.exception.BusinessException;
import com.yapp.artie.global.common.exception.ErrorResponse;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class JwtExceptionHandler extends OncePerRequestFilter {

  private final ObjectMapper objectMapper;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    try {
      filterChain.doFilter(request, response);
    } catch (BusinessException exception) {
      sendErrorMessage(response, exception);
    }
  }

  private void sendErrorMessage(HttpServletResponse response, BusinessException exception)
      throws IOException {
    setHeader(response, exception);
    writeErrorResponse(response, exception);
  }

  private void setHeader(HttpServletResponse response, BusinessException exception) {
    response.setCharacterEncoding("UTF-8");
    response.setContentType("application/json");
    response.setStatus(exception.getErrorCode().getStatus());
  }

  private void writeErrorResponse(HttpServletResponse response, BusinessException exception)
      throws IOException {
    PrintWriter writer = response.getWriter();
    writer.write(objectMapper.writeValueAsString(ErrorResponse.of(exception.getErrorCode())));
  }
}
