package com.example.Logistic_Web_App_Service_Login.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Logistic_Web_App_Service_Login.models.Token;
import com.example.Logistic_Web_App_Service_Login.models.UserLogin;

public interface TokenRepository extends JpaRepository<Token, Long>{
	List<Token> findByUserLogin(UserLogin userLogin);
	
    Token findByToken(String token);
    
    Token findByRefreshToken(String token);
    
    
}
