package com.ppg.ems_server_side_v0.service.core.implementation;
import com.ppg.ems_server_side_v0.domain.User;
import com.ppg.ems_server_side_v0.repository.UserRepository;
import com.ppg.ems_server_side_v0.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public Optional<User> findUserByEmail(String email) {

        Optional<User> user = userRepository.findByEmail(email);

        return user;
    }
}
