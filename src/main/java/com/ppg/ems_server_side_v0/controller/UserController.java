package com.ppg.ems_server_side_v0.controller;

import com.ppg.ems_server_side_v0.domain.User;
import com.ppg.ems_server_side_v0.model.api.request.UserDTO;

import com.ppg.ems_server_side_v0.model.api.response.UserResponse;
import com.ppg.ems_server_side_v0.repository.RoleRepository;
import com.ppg.ems_server_side_v0.service.core.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    private final RoleRepository roleRepository;

    @PostMapping("/user/")
    public ResponseEntity<UserResponse> addUser(@RequestBody UserDTO userDTO) {

        return ResponseEntity.ok().body(this.userService.addUser(userDTO));

    }

    @PutMapping("/updateById/{id}")
    public ResponseEntity<UserResponse> updateUser(@RequestBody UserDTO userDTO, @PathVariable String id) {

        return ResponseEntity.ok(this.userService.updateUser(userDTO, id));

    }

    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable String id) {

        this.userService.deleteUserById(id);

        return ResponseEntity.ok().body(null);

    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<UserResponse> findUserById(@PathVariable String id) {

        return ResponseEntity.ok(this.userService.findUserById(id));

    }

    @GetMapping("/findAll/")
    public ResponseEntity<List<UserResponse>> findAllUser() {

        return ResponseEntity.ok(this.userService.findAllUser());

    }

    @GetMapping("/findByEmail/1/{email}")
    public ResponseEntity<User> findUserByEmail(@PathVariable String email) {

        return ResponseEntity.ok(this.userService.findUserByEmail(email));

    }
}
