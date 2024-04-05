package com.example.Logistic_Web_App_Service_Login.services.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.example.Logistic_Web_App_Service_Login.exceptions.DataNotFoundException;
import com.example.Logistic_Web_App_Service_Login.models.User;
import com.example.Logistic_Web_App_Service_Login.responses.UserResponse;

public interface IUserService {
	User createUser(User user);
	
	User getUserById(Long userId);
	
	Page<UserResponse> getAllUserByKeyword(String keyword, PageRequest pageRequest) throws Exception; 
	
	User getUserDetailsFromToken(String token) throws Exception;

	User getUserDetailsFromRefreshToken(String refreshToken) throws Exception;
	
	void blockOrEnable(Long userLoginId, short active) throws DataNotFoundException;

}
