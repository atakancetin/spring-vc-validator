package com.cvvalidator.cvvalidator.repository;

import java.util.Optional;

import com.cvvalidator.cvvalidator.constants.RoleName;
import com.cvvalidator.cvvalidator.model.Role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
}