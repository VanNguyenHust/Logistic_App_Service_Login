package com.hust.globalict.main.services.userlogin;

import com.hust.globalict.main.modules.Token;
import com.hust.globalict.main.modules.UserLogin;

public interface IUserLoginRedisService {
	void clear(UserLogin userLogin);
	
	UserLogin getUserLoginById(Long userLoginId) throws Exception;
	
	void saveUserLoginById(Long userLoginId, UserLogin userLogin) throws Exception;
	
	UserLogin getUserLoginDetailsFromTokenOrRefreshToken(String tokenOrRefreshToken, String tokenType) throws Exception;

	void saveUserLoginByTokenOrRefreshToken(String tokenOrRefreshToken,  String tokenType, UserLogin userLogin) throws Exception;
	
	UserLogin loadUserByUsername(String username) throws Exception;
	
	void saveUserLoginByUsername(String username, UserLogin userLogin) throws Exception;
}
