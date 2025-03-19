package com.ppg.ems_server_side_v0.config.migration;

import com.ppg.ems_server_side_v0.domain.Role;
import com.ppg.ems_server_side_v0.domain.User;
import com.ppg.ems_server_side_v0.repository.RoleRepository;
import com.ppg.ems_server_side_v0.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Order(2)
@RequiredArgsConstructor
public class AdminAccountDbMigration implements ApplicationListener<Notifier> {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    @Override
    public void onApplicationEvent(Notifier event) {

        if (this.roleRepository.findRoleByRole("ADMIN").isPresent() && this.userRepository.findByRole_Role("ADMIN").isEmpty()) {
            Role role = this.roleRepository.findRoleByRole("ADMIN").orElseThrow(() -> new RuntimeException("Role not found"));
            this.userRepository.save(this.userRepository.save(User.builder().
                    userId(UUID.randomUUID().toString()).
                    email("admin@admin.com").
                    password("admin").
                    role(role).build()));

        }
    }
}
