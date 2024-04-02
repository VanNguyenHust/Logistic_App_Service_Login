package com.example.Logistic_Web_App_Service_Login.repositories;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Logistic_Web_App_Service_Login.models.Role;
import com.example.Logistic_Web_App_Service_Login.models.UserLogin;
import com.example.Logistic_Web_App_Service_Login.models.User;


public interface UserLoginRepository extends JpaRepository<UserLogin, Long> {
	boolean existsByUsername(String username);

	Set<UserLogin> findByRole(Role role);

	UserLogin findByUsernameAndLoginType(String username, String loginType);

	Optional<UserLogin> findByUsername(String username);
	
	boolean existsByLoginTypeAndUser(String loginType, User user);
}
