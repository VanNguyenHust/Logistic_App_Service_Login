package com.example.Logistic_Web_App_Service_Login.services.userlogin;

import com.example.Logistic_Web_App_Service_Login.exceptions.DataNotFoundException;
import com.example.Logistic_Web_App_Service_Login.exceptions.InvalidPasswordException;
import com.example.Logistic_Web_App_Service_Login.models.UserLogin;

public interface IUserLoginService {
	UserLogin createUserLogin(UserLogin userLogin) throws Exception;

	UserLogin getUserLoginById(Long userLoginId);
	
	String login(String userName, String password, String loginType) throws Exception;

	UserLogin getUserLoginDetailsFromToken(String token) throws Exception;

	UserLogin getUserLoginDetailsFromRefreshToken(String refreshToken) throws Exception;
	
	void resetPassword(Long userLoginId, String newPassword) throws InvalidPasswordException, DataNotFoundException;
}
