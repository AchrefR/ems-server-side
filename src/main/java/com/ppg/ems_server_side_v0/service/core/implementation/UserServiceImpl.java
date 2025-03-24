package com.ppg.ems_server_side_v0.service.core.implementation;

import com.ppg.ems_server_side_v0.domain.Role;
import com.ppg.ems_server_side_v0.domain.User;
import com.ppg.ems_server_side_v0.model.api.request.UserDTO;
import com.ppg.ems_server_side_v0.repository.RoleRepository;
import com.ppg.ems_server_side_v0.repository.UserRepository;
import com.ppg.ems_server_side_v0.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public User addUser(UserDTO userDTO) {

        Role role = this.roleRepository.findRoleByRole(userDTO.getRole()).orElseThrow(() -> new RuntimeException("Role not found"));

        User user = User.builder().email(userDTO.getEmail()).
                password(this.passwordEncoder.encode(userDTO.getPassword())).
                role(role).
                build();

        if (!this.userRepository.findByEmail(user.getEmail()).isPresent()) {

            return this.userRepository.save(user);

        } else {

            throw new RuntimeException("User already exists");

        }
    }

    @Override
    public User updateUser(UserDTO userDTO, String id) {

        User user = this.userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

        user.setEmail(userDTO.getEmail());

        user.setPassword(userDTO.getPassword());

        user.setRole(this.roleRepository.
                findRoleByRole(userDTO.getRole()).
                orElseThrow(() -> new RuntimeException("Role not found")));

        return this.userRepository.save(user);
    }

    @Override
    public void deleteUserById(String id) {

        this.userRepository.deleteById(id);
    }

    @Override
    public User findUserById(String id) {

        return this.userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

    }

    @Override
    public List<User> findAllUser() {

        return this.userRepository.findAll();

    }

    @Override
    public User findUserByEmail(String email) {

        return this.userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

    }

}
