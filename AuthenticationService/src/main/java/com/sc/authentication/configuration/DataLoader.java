package com.sc.authentication.configuration;

import com.sc.authentication.model.Roles;
import com.sc.authentication.repository.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DataLoader {

    @Bean
    public CommandLineRunner loadRolesInDatabase(RoleRepository roleRepository) {
        return args -> {
            if (!roleRepository.existsByRoleName("ROLE_ADMIN")) {
                Roles roleAdmin = roleRepository.save(Roles.builder().roleName("ROLE_ADMIN")
                    .description("Administrator access").build());
                log.info("ADMIN ROLE added::{}", roleAdmin);
            }
            if (!roleRepository.existsByRoleName("ROLE_USER")) {
                Roles roleUser = roleRepository.save(Roles.builder().roleName("ROLE_USER")
                    .description("Administrator access").build());
                log.info("USER ROLE added::{}", roleUser);
            }
        };
    }
}