package com.example.Logistic_Web_App_Service_Login.common.repository;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Logistic_Web_App_Service_Login.common.entity.Role;
import com.example.Logistic_Web_App_Service_Login.common.entity.UserLogin;

public interface UserLoginRepository extends JpaRepository<UserLogin, Long>{
	boolean existsByUserName(String userName);
	
	Set<UserLogin> findByRole(Role role);
	
	Optional<UserLogin> findByUserName(String username);
}
