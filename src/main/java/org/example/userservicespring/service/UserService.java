package org.example.userservicespring.service;

import lombok.RequiredArgsConstructor;
import org.example.userservicespring.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

}
