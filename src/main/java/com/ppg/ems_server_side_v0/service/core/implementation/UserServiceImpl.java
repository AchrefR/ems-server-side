package com.ppg.ems_server_side_v0.service.core.implementation;

import com.ppg.ems_server_side_v0.domain.Role;
import com.ppg.ems_server_side_v0.domain.User;
import com.ppg.ems_server_side_v0.mapper.UserMapper;
import com.ppg.ems_server_side_v0.model.api.request.UserDTO;
import com.ppg.ems_server_side_v0.model.api.response.UserResponse;
import com.ppg.ems_server_side_v0.repository.RoleRepository;
import com.ppg.ems_server_side_v0.repository.UserRepository;
import com.ppg.ems_server_side_v0.service.core.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;

    @Override
    public UserResponse addUser(UserDTO userDTO) {

        Role role = this.roleRepository.findRoleByRole(userDTO.role()).orElseThrow(() -> new RuntimeException("Role not found"));

        User user = User.builder().email(userDTO.email()).
                password(this.passwordEncoder.encode(userDTO.password())).
                role(role).
                build();

        if (!this.userRepository.findByEmail(user.getEmail()).isPresent()) {

            return this.userMapper.userMapper(this.userRepository.save(user));

        } else {

            throw new RuntimeException("User already exists");

        }
    }

    @Override
    public UserResponse updateUser(UserDTO userDTO, String id) {

        User user = this.userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

        user.setEmail(userDTO.email());

        user.setPassword(userDTO.password());

        user.setRole(this.roleRepository.
                findRoleByRole(userDTO.role()).
                orElseThrow(() -> new RuntimeException("Role not found")));

        return this.userMapper.userMapper(this.userRepository.save(user));
    }

    @Override
    public void deleteUserById(String id) {

        this.userRepository.deleteById(id);
    }

    @Override
    public UserResponse findUserById(String id) {

        User user = this.userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

        return this.userMapper.userMapper(user);

    }

    @Override
    public List<UserResponse> findAllUser() {

        return this.userMapper.allUsersMapper(this.userRepository.findAll());

    }

    @Override
    public User findUserByEmail(String email) {

        return this.userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

    }

}
