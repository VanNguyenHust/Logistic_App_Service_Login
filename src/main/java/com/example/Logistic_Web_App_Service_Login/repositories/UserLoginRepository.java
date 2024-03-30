package com.example.Logistic_Web_App_Service_Login.repositories;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Logistic_Web_App_Service_Login.models.Role;
import com.example.Logistic_Web_App_Service_Login.models.UserLogin;



public interface UserLoginRepository extends JpaRepository<UserLogin, Long>{
	boolean existsByUserName(String userName);
	
	Set<UserLogin> findByRole(Role role);
	
	Optional<UserLogin> findByUserNameAndLoginType(String userName, String loginType);
	
	Optional<UserLogin> findByUserName(String userName);
}
