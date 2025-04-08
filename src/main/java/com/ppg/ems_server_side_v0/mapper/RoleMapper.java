package com.ppg.ems_server_side_v0.mapper;

import com.ppg.ems_server_side_v0.domain.Role;
import com.ppg.ems_server_side_v0.model.api.response.RoleResponse;
import com.ppg.ems_server_side_v0.repository.AccessRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
@RequiredArgsConstructor
public class RoleMapper {

    private final AccessRepository accessRepository;

    public RoleResponse toRoleReponse(Role role) {
        List<String> accesses = new ArrayList<>();
        role.getRole_accesses().forEach(role_access -> {
            String access = this.accessRepository.findById(role_access.getAccess().getId()).orElseThrow(() -> new RuntimeException("Role not found")).getTitle();
            accesses.add(access);
        });
        RoleResponse roleResponse = new RoleResponse(
                role.getId(),
                role.getRole(),
                accesses);
        return roleResponse;
    }

    public List<RoleResponse> toRoleReponseList(List<Role> roles) {
        List<RoleResponse> roleResponses = new ArrayList<>();
        roles.forEach(role -> {
            RoleResponse roleResponse = this.toRoleReponse(role);
            roleResponses.add(roleResponse);
        });
        return roleResponses;

    }



}
