package com.protifolio.taskkiller.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.protifolio.taskkiller.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
	Role findByName(String roleName);
}
