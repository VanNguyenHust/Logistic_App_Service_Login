package com.hust.globalict.main.services.userlogin;

import com.hust.globalict.main.exceptions.DataNotFoundException;
import com.hust.globalict.main.exceptions.InvalidPasswordException;
import com.hust.globalict.main.modules.UserLogin;

public interface IUserLoginService {
	UserLogin createUserLogin(UserLogin userLogin) throws Exception;

	UserLogin getUserLoginById(Long userLoginId) throws Exception;
	
	String login(String userName, String password, String loginType) throws Exception;

	UserLogin getUserLoginDetailsFromToken(String token, String tokenType) throws Exception;

	void resetPassword(Long userLoginId, String newPassword) throws Exception;

	UserLogin getUserLoginDetailsFromRefreshToken(String refreshToken, String tokenType) throws Exception;
}
