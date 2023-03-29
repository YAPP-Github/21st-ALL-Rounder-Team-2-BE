package com.yapp.artie.domain.user.application.port.in;

import com.yapp.artie.domain.user.domain.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface GetUserQuery extends UserDetailsService {

  User loadUserById(Long userId);

  UserDetails loadUserByUsername(String uid);
}
