package com.example.Logistic_Web_App_Service_Login.services.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.Logistic_Web_App_Service_Login.exceptions.DataNotFoundException;
import com.example.Logistic_Web_App_Service_Login.mappers.UserMapper;
import com.example.Logistic_Web_App_Service_Login.models.User;
import com.example.Logistic_Web_App_Service_Login.repositories.UserRepository;
import com.example.Logistic_Web_App_Service_Login.responses.UserResponse;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
	private final UserRepository userRepository;

	@Autowired
	UserMapper userMapper;

	@Override
	@Transactional
	public User createUser(User user) {
		return userRepository.save(user);
	}

	@Override
	public User getUserById(Long userId) {
		return userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
	}

	@Override
	public User getUserDetailsFromToken(String token) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User getUserDetailsFromRefreshToken(String refreshToken) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void blockOrEnable(Long userLoginId, short active) throws DataNotFoundException {
		// TODO Auto-generated method stub

	}

	@Override
	public Page<UserResponse> getAllUserByKeyword(String keyword, PageRequest pageRequest) throws Exception {
		Page<User> userPage = userRepository.findAllUserByKeyword(keyword, pageRequest);
		
		return userPage.map(user -> userMapper.mapToUserReponse(user));
	}

}
