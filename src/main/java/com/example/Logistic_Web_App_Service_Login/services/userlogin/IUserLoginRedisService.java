package com.example.Logistic_Web_App_Service_Login.services.userlogin;

import com.example.Logistic_Web_App_Service_Login.models.Token;
import com.example.Logistic_Web_App_Service_Login.models.UserLogin;
import com.example.Logistic_Web_App_Service_Login.responses.UserLoginResponse;

public interface IUserLoginRedisService {
	void clear(); 
	
	UserLoginResponse getUserLogin(Long userLoginId) throws Exception;
	
	void saveUserLogin(Long userLoginId, UserLoginResponse userLoginResponse) throws Exception;
	
	UserLogin getUserLoginDetailsFromToken(String token, String tokenType) throws Exception;
	
	UserLogin getUserLoginDetailsFromRefreshToken(String refreshToken, String tokenType) throws Exception;
	
	void saveToken(Token token, UserLogin userLogin) throws Exception;
	
	UserLoginResponse loadUserByUsername(String username) throws Exception;
	
	void saveUserLoginByUsername(String username, UserLoginResponse userLogin) throws Exception;
}
