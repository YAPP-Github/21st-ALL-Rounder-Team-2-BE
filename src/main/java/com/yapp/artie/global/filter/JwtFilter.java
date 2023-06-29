package com.yapp.artie.global.filter;

import com.yapp.artie.user.application.port.out.TokenParsingPort;
import com.yapp.artie.user.domain.ArtieToken;
import java.io.IOException;
import java.util.NoSuchElementException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

  private final UserDetailsService userDetailsService;
  private final TokenParsingPort tokenParsingPort;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    setTokenAtSecurityContext(tokenParsingPort.parseToken(request.getHeader("Authorization")));
    filterChain.doFilter(request, response);
  }

  private void setTokenAtSecurityContext(ArtieToken decodedToken) throws NoSuchElementException {
    UserDetails user = userDetailsService.loadUserByUsername(decodedToken.getUid());
    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
        user, user.getPassword(), user.getAuthorities());
    SecurityContext context = SecurityContextHolder.getContext();

    context.setAuthentication(authenticationToken);
  }
}
