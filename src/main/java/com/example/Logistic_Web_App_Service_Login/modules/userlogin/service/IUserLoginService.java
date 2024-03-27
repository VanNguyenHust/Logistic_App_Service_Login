package com.example.Logistic_Web_App_Service_Login.modules.userlogin.service;

import com.example.Logistic_Web_App_Service_Login.common.entity.UserLogin;
import com.example.Logistic_Web_App_Service_Login.common.exceptions.DataNotFoundException;
import com.example.Logistic_Web_App_Service_Login.common.exceptions.InvalidPasswordException;
import com.example.Logistic_Web_App_Service_Login.modules.userlogin.dto.UserLoginDTO;

public interface IUserLoginService {
	UserLogin createUserLogin(UserLoginDTO userLoginDTO) throws Exception;

	UserLogin getUserById(Long userId);
	
	String login(String userName, String password, Long roleId) throws Exception;

	void resetPassword(Long userLoginId, String newPassword) throws InvalidPasswordException, DataNotFoundException;
}
