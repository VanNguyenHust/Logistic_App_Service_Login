package com.example.Logistic_Web_App_Service_Login.services.user;

import java.util.List;

import org.springframework.data.domain.PageRequest;

import com.example.Logistic_Web_App_Service_Login.models.User;
import com.example.Logistic_Web_App_Service_Login.responses.UserResponse;

public interface IUserRedisService {
	void clearByUserId(Long userId);
	
	List<UserResponse> getAllUserByKeyword(String keyword, PageRequest pageRequest) throws Exception;
	
	void saveAllUserByKeyword(List<UserResponse> userResponses, String keyword, PageRequest pageRequest) throws Exception;
	
	User getUserById(Long userId) throws Exception;
	
	void saveUserById(Long userId, User user) throws Exception;
}
