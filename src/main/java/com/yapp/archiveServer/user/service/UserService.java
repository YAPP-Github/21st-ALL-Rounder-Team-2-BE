package com.yapp.archiveServer.user.service;

import com.yapp.archiveServer.user.domain.User;
import com.yapp.archiveServer.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Optional<User> findByUid(String uid) {
        return userRepository.findByUid(uid);
    }

}
