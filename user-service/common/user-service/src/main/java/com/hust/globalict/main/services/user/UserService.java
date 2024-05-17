package com.hust.globalict.main.services.user;

import com.hust.globalict.main.utils.MessageKeyExceptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.hust.globalict.main.components.LocalizationUtils;
import com.hust.globalict.main.exceptions.DataNotFoundException;
import com.hust.globalict.main.mappers.UserMapper;
import com.hust.globalict.main.modules.User;
import com.hust.globalict.main.repositories.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
	private final UserRepository userRepository;
	private final IUserRedisService userRedisService;

	private final LocalizationUtils localizationUtils;

	@Autowired
	UserMapper userMapper;

	@Override
	@Transactional
	public User createUser(User user) throws Exception {
		userRepository.save(user);
		userRedisService.saveUserById(user.getId(), user);

		return user;
	}

	@Override
	public User getUserById(Long userId) throws Exception {
		User user = userRedisService.getUserById(userId);
		if (user == null) {
			user = userRepository.findById(userId).orElseThrow(() ->
					new RuntimeException(localizationUtils.getLocalizedMessage(MessageKeyExceptions.USER_NOT_FOUND)));
			
			userRedisService.saveUserById(userId, user);
		}
		
		return user;
	}

	@Override
	public void blockOrEnable(Long userLoginId, short active) throws DataNotFoundException {
	}

	@Override
	public List<User> getAllUserByKeyword(String keyword, PageRequest pageRequest) throws Exception {
		List<User> userPage = userRedisService.getAllUserByKeyword(keyword, pageRequest);
		if (userPage == null) {
			userPage = userRepository.findAllUserByKeyword(keyword, pageRequest);

			userRedisService.saveAllUserByKeyword(userPage, keyword, pageRequest);
		}
		
		return userPage;
	}

	@Override
	public User updateUser(User user) throws Exception {
		user = userRepository.save(user);
		userRedisService.saveUserById(user.getId(), user);

		return user;
	}

}
