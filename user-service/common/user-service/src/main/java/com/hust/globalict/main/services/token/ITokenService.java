package com.hust.globalict.main.services.token;

import com.hust.globalict.main.modules.Token;
import com.hust.globalict.main.modules.UserLogin;

public interface ITokenService {
	Token addToken(UserLogin userLogin, String token, boolean isMobileDevice);
	
    Token refreshToken(String refreshToken, UserLogin userLogin) throws Exception;
}
