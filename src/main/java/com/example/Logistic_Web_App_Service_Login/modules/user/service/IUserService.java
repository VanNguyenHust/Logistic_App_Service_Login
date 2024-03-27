package com.example.Logistic_Web_App_Service_Login.modules.user.service;

import com.example.Logistic_Web_App_Service_Login.common.entity.User;
import com.example.Logistic_Web_App_Service_Login.common.exceptions.DataNotFoundException;
import com.example.Logistic_Web_App_Service_Login.modules.user.dto.UserDTO;

public interface IUserService {
	User createUser(UserDTO userDTO);
	
	User getUserById(Long userLoginId);
	
	User getUserDetailsFromToken(String token) throws Exception;

	User getUserDetailsFromRefreshToken(String refreshToken) throws Exception;
	
	void blockOrEnable(Long userLoginId, short active) throws DataNotFoundException;
}
