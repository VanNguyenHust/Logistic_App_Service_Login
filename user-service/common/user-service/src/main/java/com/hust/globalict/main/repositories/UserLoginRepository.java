package com.hust.globalict.main.repositories;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hust.globalict.main.modules.Role;
import com.hust.globalict.main.modules.User;
import com.hust.globalict.main.modules.UserLogin;


public interface UserLoginRepository extends JpaRepository<UserLogin, Long> {
	boolean existsByUsername(String username);

	Set<UserLogin> findByRole(Role role);

	UserLogin findByUsernameAndLoginType(String username, String loginType);

	UserLogin findByUsername(String username);
	
	boolean existsByLoginTypeAndUser(String loginType, User user);
}
