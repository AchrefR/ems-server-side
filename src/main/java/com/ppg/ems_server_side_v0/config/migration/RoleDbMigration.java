package com.ppg.ems_server_side_v0.config.migration;

import com.ppg.ems_server_side_v0.domain.*;
import com.ppg.ems_server_side_v0.domain.enums.DefaultRole;
import com.ppg.ems_server_side_v0.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@Order(2)
@RequiredArgsConstructor
public class RoleDbMigration implements ApplicationListener<Notifier> {

    private final RoleRepository roleRepository;

    public List<DefaultRole> roleNames = List.of(DefaultRole.values());

    @Override
    public void onApplicationEvent(Notifier event) {

        if (this.roleRepository.findRoleByRole("ADMIN").isEmpty()) {

            roleNames.forEach(roleName -> {

                Role role = Role.builder().role(roleName.toString()).build();
                this.roleRepository.save(role);
            });
        }


    }
}
