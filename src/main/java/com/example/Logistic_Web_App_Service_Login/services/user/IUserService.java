package com.example.Logistic_Web_App_Service_Login.services.user;

import com.example.Logistic_Web_App_Service_Login.dtos.UserDTO;
import com.example.Logistic_Web_App_Service_Login.exceptions.DataNotFoundException;
import com.example.Logistic_Web_App_Service_Login.models.User;

public interface IUserService {
	User createUser(UserDTO userDTO);
	
	User getUserById(Long userLoginId);
	
	User getUserDetailsFromToken(String token) throws Exception;

	User getUserDetailsFromRefreshToken(String refreshToken) throws Exception;
	
	void blockOrEnable(Long userLoginId, short active) throws DataNotFoundException;
}
