package com.ppg.ems_server_side_v0.config.migration;

import com.ppg.ems_server_side_v0.domain.Access;
import com.ppg.ems_server_side_v0.domain.Role;
import com.ppg.ems_server_side_v0.domain.Role_Access;
import com.ppg.ems_server_side_v0.repository.AccessRepository;
import com.ppg.ems_server_side_v0.repository.RoleRepository;
import com.ppg.ems_server_side_v0.repository.Role_AccessRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Order(3)
@RequiredArgsConstructor
public class Access_RoleDdMigration implements ApplicationListener<Notifier> {

    private final AccessRepository accessRepository;

    private final RoleRepository roleRepository;

    private final Role_AccessRepository role_accessRepository;

    @Override
    public void onApplicationEvent(Notifier event) {

        if (this.role_accessRepository.findAll().isEmpty()) {
            List<Access> accesses = this.accessRepository.findAll();

            List<Role> roles = this.roleRepository.findAll();

            List<Role_Access> role_accesses = new ArrayList<>();

            roles.forEach(role -> {

                accesses.forEach(access -> {
                    Role_Access role_access = Role_Access.builder().role(role).access(access).build();
                    this.role_accessRepository.save(role_access);
                    role_accesses.add(role_access);
                });
//                role.setRole_accesses(role_accesses);
                this.roleRepository.save(role);
            });

        }


    }
}
