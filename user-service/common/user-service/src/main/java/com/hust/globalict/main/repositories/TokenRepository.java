package com.hust.globalict.main.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hust.globalict.main.modules.Token;
import com.hust.globalict.main.modules.UserLogin;

public interface TokenRepository extends JpaRepository<Token, Long>{
	List<Token> findByUserLogin(UserLogin userLogin);
	
    Token findByToken(String token);
    
    Token findByRefreshToken(String token); 
    
}
