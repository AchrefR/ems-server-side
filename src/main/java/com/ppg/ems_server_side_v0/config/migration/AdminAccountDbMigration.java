package com.ppg.ems_server_side_v0.config.migration;

import com.ppg.ems_server_side_v0.domain.Address;
import com.ppg.ems_server_side_v0.domain.Person;
import com.ppg.ems_server_side_v0.domain.Role;
import com.ppg.ems_server_side_v0.domain.User;
import com.ppg.ems_server_side_v0.domain.enums.PersonType;
import com.ppg.ems_server_side_v0.repository.AddressRepository;
import com.ppg.ems_server_side_v0.repository.PersonRepository;
import com.ppg.ems_server_side_v0.repository.RoleRepository;
import com.ppg.ems_server_side_v0.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Order(4)
@RequiredArgsConstructor
public class AdminAccountDbMigration implements ApplicationListener<Notifier> {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final AddressRepository addressRepository;

    private final PersonRepository personRepository;

    @Override
    public void onApplicationEvent(Notifier event) {

        if (this.roleRepository.findRoleByRole("ADMIN").isPresent() && this.userRepository.findByRole_Role("ADMIN").isEmpty()) {
            Role role = this.roleRepository.findRoleByRole("ADMIN").orElseThrow(() -> new RuntimeException("Role not found"));

            Address address = this.addressRepository.save(
                    Address.builder().
                            streetName("Admin Street").
                            zipCode("12345").
                            state("Admin State").
                            town("Admin Town").build());

            Person person = this.personRepository.save(
                    Person.builder().
                            firstName("Admin").
                            lastName("Admin").
                            birthDate("2000-01-01").
                            phoneNumber("1234567890").
                            personType(PersonType.EMPLOYEE.name()).
                            address(address).build());

            this.userRepository.save(User.builder().
                    id(UUID.randomUUID().toString()).
                    email("admin@admin.com").
                    password(this.passwordEncoder.encode("admin")).
                    role(role).
                    person(person)
                    .build());

        }
    }
}
