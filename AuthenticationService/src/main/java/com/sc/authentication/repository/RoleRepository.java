package com.sc.authentication.repository;

import com.sc.authentication.model.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Roles, String> {

    Boolean existsByRoleName(String roleName);
}