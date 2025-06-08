package com.ppg.ems_server_side_v0.controller;

import com.ppg.ems_server_side_v0.domain.Role;
import com.ppg.ems_server_side_v0.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleRepository roleRepository;

    @GetMapping("/")
    public ResponseEntity<List<Map<String, String>>> findAllRoles() {
        List<Map<String, String>> roles = this.roleRepository.findAll()
                .stream()
                .map(role -> Map.of(
                        "id", role.getId(),
                        "role", role.getRole()
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(roles);
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<Role> findRoleById(@PathVariable String id) {
        Role role = this.roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        return ResponseEntity.ok(role);
    }

    @GetMapping("/findByName/{name}")
    public ResponseEntity<Role> findRoleByName(@PathVariable String name) {
        Role role = this.roleRepository.findRoleByRole(name)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        return ResponseEntity.ok(role);
    }
}
