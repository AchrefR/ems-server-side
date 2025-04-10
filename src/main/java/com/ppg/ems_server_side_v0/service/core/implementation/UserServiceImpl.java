package com.ppg.ems_server_side_v0.service.core.implementation;

import com.ppg.ems_server_side_v0.domain.Person;
import com.ppg.ems_server_side_v0.domain.Role;
import com.ppg.ems_server_side_v0.domain.User;
import com.ppg.ems_server_side_v0.mapper.UserMapper;
import com.ppg.ems_server_side_v0.model.api.request.UserDTO;
import com.ppg.ems_server_side_v0.model.api.response.UserResponse;
import com.ppg.ems_server_side_v0.repository.PersonRepository;
import com.ppg.ems_server_side_v0.repository.RoleRepository;
import com.ppg.ems_server_side_v0.repository.UserRepository;
import com.ppg.ems_server_side_v0.service.core.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PersonRepository personRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;

    @Override
    public UserResponse addUser(UserDTO userDTO) {

        Role role = this.roleRepository.findById(userDTO.roleId()).orElseThrow(() -> new RuntimeException("Role not found"));

        Person person = this.personRepository.findById(userDTO.personId()).orElseThrow(() -> new RuntimeException("Person not found"));

        User user = User.builder().
                email(userDTO.email()).
                password(this.passwordEncoder.encode(userDTO.password())).
                role(role).
                person(person).
                build();

        if (!this.userRepository.findByEmail(user.getEmail()).isPresent()) {

            return this.userMapper.toUserReponse(this.userRepository.save(user));

        } else {

            throw new RuntimeException("User already exists");

        }
    }

    @Override
    public UserResponse updateUser(UserDTO userDTO, String id) {

        User user = this.userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setEmail(userDTO.email());

        return this.userMapper.toUserReponse(this.userRepository.save(user));
    }

    @Override
    public void deleteUserById(String id) {

        this.userRepository.deleteById(id);
    }

    @Override
    public UserResponse findUserById(String id) {

        User user = this.userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

        return this.userMapper.toUserReponse(user);

    }

    @Override
    public List<UserResponse> findAllUser() {

        return this.userMapper.toUserReponseList(this.userRepository.findAll());

    }

    @Override
    public User findUserByEmail(String email) {

        return this.userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

    }

}
