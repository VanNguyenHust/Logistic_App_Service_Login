package com.example.Logistic_Web_App_Service_Login.services.token;

import com.example.Logistic_Web_App_Service_Login.models.Token;
import com.example.Logistic_Web_App_Service_Login.models.UserLogin;

public interface ITokenService {
	Token addToken(UserLogin userLogin, String token, boolean isMobileDevice);
	
    Token refreshToken(String refreshToken, UserLogin userLogin) throws Exception;
}
