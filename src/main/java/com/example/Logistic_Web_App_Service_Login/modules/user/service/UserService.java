package com.example.Logistic_Web_App_Service_Login.modules.user.service;

import org.springframework.stereotype.Service;

import com.example.Logistic_Web_App_Service_Login.common.entity.User;
import com.example.Logistic_Web_App_Service_Login.common.exceptions.DataNotFoundException;
import com.example.Logistic_Web_App_Service_Login.common.repository.UserRepository;
import com.example.Logistic_Web_App_Service_Login.modules.user.dto.UserDTO;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
	private final UserRepository userRepository;
	
	@Override
	@Transactional
	public User createUser(UserDTO userDTO) {
		User newUser = User.builder()
				.fullName(userDTO.getFullName())
				.isActive(userDTO.getIsActive())
				.build();
		
		return userRepository.save(newUser);
	}

	@Override
	public User getUserById(Long userid) {
		return userRepository.findById(userid)
				.orElseThrow(() -> new RuntimeException("User not found"));
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

}
