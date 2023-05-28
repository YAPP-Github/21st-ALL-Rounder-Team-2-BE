package com.yapp.artie.domain.user.application.port.in.query;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface GetUserDetailsQuery extends UserDetailsService {

  UserDetails loadUserByUsername(String uid);
}
