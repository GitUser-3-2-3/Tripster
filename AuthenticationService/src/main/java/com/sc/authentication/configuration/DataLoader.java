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
                roleRepository.save(Roles.builder().roleName("ROLE_ADMIN")
                    .description("Administrator access").build()
                );
            }
            if (roleRepository.existsByRoleName("ROLE_USER")) {
                roleRepository.save(Roles.builder().roleName("ROLE_USER")
                    .description("Administrator access").build()
                );
            }
        };
    }
}