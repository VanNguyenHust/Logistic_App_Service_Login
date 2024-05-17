package com.hust.globalict.main.services.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.hust.globalict.main.exceptions.DataNotFoundException;
import com.hust.globalict.main.modules.User;

import java.util.List;

public interface IUserService {
	User createUser(User user) throws Exception;
	
	User getUserById(Long userId) throws Exception;
	
	List<User> getAllUserByKeyword(String keyword, PageRequest pageRequest) throws Exception;

	void blockOrEnable(Long userLoginId, short active) throws DataNotFoundException;
	
	User updateUser(User user) throws Exception;
}
