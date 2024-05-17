package com.hust.globalict.main.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hust.globalict.main.modules.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
	boolean existsByName(String name);

	Role findByName(String roleName);

}
