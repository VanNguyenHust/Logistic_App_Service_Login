package com.hust.globalict.main.services.user;

import java.util.List;

import org.springframework.data.domain.PageRequest;

import com.hust.globalict.main.modules.User;

public interface IUserRedisService {
	void clearUser(User user);
	
	List<User> getAllUserByKeyword(String keyword, PageRequest pageRequest) throws Exception;
	
	void saveAllUserByKeyword(List<User> users, String keyword, PageRequest pageRequest) throws Exception;
	
	User getUserById(Long userId) throws Exception;
	
	void saveUserById(Long userId, User user) throws Exception;
}
