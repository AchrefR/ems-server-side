package com.ppg.ems_server_side_v0.config.migration;

import com.ppg.ems_server_side_v0.domain.Role;
import com.ppg.ems_server_side_v0.domain.enums.DefaultRole;
import com.ppg.ems_server_side_v0.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@Order(1)
@RequiredArgsConstructor
public class RoleDbMigration implements ApplicationListener<Notifier> {

    private final RoleRepository roleRepository;

    public List<DefaultRole> roleNames = List.of(DefaultRole.values());

    @Override
    public void onApplicationEvent(Notifier event) {
        if (this.roleRepository.findRoleByRole("ADMIN").isEmpty()) {
            roleNames.forEach(roleName -> {
                this.roleRepository.save(Role.builder().
                        roleId(UUID.randomUUID().toString())
                        .role(roleName.toString()).build());
            });
        }

    }
}
