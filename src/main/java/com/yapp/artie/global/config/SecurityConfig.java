package com.yapp.artie.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yapp.artie.domain.user.application.port.out.TokenParsingPort;
import com.yapp.artie.global.filter.JwtExceptionHandler;
import com.yapp.artie.global.filter.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private final UserDetailsService userDetailsService;
  private final TokenParsingPort tokenParsingPort;

  @Override
  public void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests()
        .anyRequest()
        .authenticated().and()
        .addFilterBefore(new JwtFilter(userDetailsService, tokenParsingPort),
            UsernamePasswordAuthenticationFilter.class)
        .addFilterBefore(new JwtExceptionHandler(new ObjectMapper()), JwtFilter.class);

    http.csrf().disable()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    http.exceptionHandling()
        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
  }

  @Override
  public void configure(WebSecurity web) throws Exception {
    web.ignoring().antMatchers(HttpMethod.POST, "/user")
        .antMatchers("/")
        .antMatchers("/swagger-ui/**")
        .antMatchers("/v3/api-docs/**")
        .antMatchers("/resources/**")
        .antMatchers("/user/test/token");
  }
}