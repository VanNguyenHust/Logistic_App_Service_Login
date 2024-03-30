package com.example.Logistic_Web_App_Service_Login.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Logistic_Web_App_Service_Login.models.Role;


public interface RoleRepository extends JpaRepository<Role, Long>{
	List<Role> findByName(String name);
}
