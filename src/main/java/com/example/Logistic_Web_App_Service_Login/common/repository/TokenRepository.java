package com.example.Logistic_Web_App_Service_Login.common.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Logistic_Web_App_Service_Login.common.entity.Token;
import com.example.Logistic_Web_App_Service_Login.common.entity.UserLogin;

public interface TokenRepository extends JpaRepository<Token, Long>{
	List<Token> findByUserLogin(UserLogin userLogin);
	
    Token findByToken(String token);
    
    Token findByRefreshToken(String token);
    
    
}
