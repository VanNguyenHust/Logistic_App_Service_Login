package com.example.Logistic_Web_App_Service_Login.modules.token.service;

import com.example.Logistic_Web_App_Service_Login.common.entity.Token;
import com.example.Logistic_Web_App_Service_Login.common.entity.UserLogin;

public interface ITokenService {
	Token addToken(UserLogin userLogin, String token, boolean isMobileDevice);
	
    Token refreshToken(String refreshToken, UserLogin userLogin) throws Exception;
}
