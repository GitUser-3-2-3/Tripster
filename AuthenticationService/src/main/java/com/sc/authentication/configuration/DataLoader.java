package com.sc.authentication.configuration;

import com.sc.authentication.model.Roles;
import com.sc.authentication.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class DataLoader {

    @Bean
    CommandLineRunner loadRolesInDatabase(RoleRepository roleRepository) {
        return args -> {
            if (roleRepository.existsByRoleName("ROLE_ADMIN")) {
                Roles adminRole = Roles.builder().roleName("ROLE_ADMIN")
                    .description("Administrator access")
                    .build();
                roleRepository.save(adminRole);
            }
            if (roleRepository.existsByRoleName("ROLE_USER")) {
                Roles userRole = Roles.builder().roleName("ROLE_USER")
                    .description("Administrator access")
                    .build();
                roleRepository.save(userRole);
            }
        };
    }
}