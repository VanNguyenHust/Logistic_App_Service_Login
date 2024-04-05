package com.example.Logistic_Web_App_Service_Login.services.userlogin;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.Logistic_Web_App_Service_Login.components.JwtTokenUtil;
import com.example.Logistic_Web_App_Service_Login.components.LocalizationUtils;
import com.example.Logistic_Web_App_Service_Login.enums.StatusRole;
import com.example.Logistic_Web_App_Service_Login.exceptions.DataNotFoundException;
import com.example.Logistic_Web_App_Service_Login.exceptions.ExpiredTokenException;
import com.example.Logistic_Web_App_Service_Login.exceptions.InvalidPasswordException;
import com.example.Logistic_Web_App_Service_Login.mappers.UserLoginMapper;
import com.example.Logistic_Web_App_Service_Login.models.Token;
import com.example.Logistic_Web_App_Service_Login.models.UserLogin;
import com.example.Logistic_Web_App_Service_Login.repositories.TokenRepository;
import com.example.Logistic_Web_App_Service_Login.repositories.UserLoginRepository;
import com.example.Logistic_Web_App_Service_Login.utils.MessageKeys;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserLoginService implements IUserLoginService {
	private final UserLoginRepository userLoginRepository;
	private final AuthenticationManager authenticationManager;

	private final JwtTokenUtil jwtTokenUtil;
	private final TokenRepository tokenRepository;
	private final PasswordEncoder passwordEncoder;

	@Autowired
	UserLoginMapper userLoginMapper;

	private final LocalizationUtils localizationUtils;

	@Override
	@Transactional
	public UserLogin createUserLogin(UserLogin userLogin) throws Exception {
		if (userLoginRepository.existsByLoginTypeAndUser(userLogin.getLoginType(), userLogin.getUser())) {
			throw new DataIntegrityViolationException(localizationUtils
					.getLocalizedMessage(MessageKeys.USER_LOGIN_CREATE_ACCOUNT_ALREADY_REGISTERED_WITH_METHOD));
		}

		if (userLoginRepository.existsByUsername(userLogin.getUsername())) {
			throw new DataIntegrityViolationException(
					localizationUtils.getLocalizedMessage(MessageKeys.CREATE_USER_LOGIN_USERNAME_EXIST));
		}

		if (userLogin.getRole().getName().toUpperCase().equals(StatusRole.ADMIN)) {
			throw new Exception(localizationUtils.getLocalizedMessage(MessageKeys.CREATE_USER_NOT_ADMIN));
		}

		String encodedPassword = passwordEncoder.encode(userLogin.getPassword());
		userLogin.setPassword(encodedPassword);

		return userLoginRepository.save(userLogin);
	}

	@Override
	public UserLogin getUserLoginById(Long userLoginId) {
		return userLoginRepository.findById(userLoginId)
				.orElseThrow(() -> new RuntimeException(String.format("User with id = %d not found", userLoginId)));
	}

	@Override
	public String login(String username, String password, String loginType) throws Exception {
		UserLogin existingUserLogin = userLoginRepository.findByUsernameAndLoginType(username, loginType);

		if (existingUserLogin == null) {
			throw new DataNotFoundException("Invalid phone number or password");
		}

		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,
				password);

		authenticationManager.authenticate(authenticationToken);

		return jwtTokenUtil.generateToken(existingUserLogin);
	}

	@Override
	@Transactional
	public void resetPassword(Long userLoginId, String newPassword)
			throws InvalidPasswordException, DataNotFoundException {
		UserLogin existingUserLogin = getUserLoginById(userLoginId);

		// reset password => clear token
		List<Token> tokens = tokenRepository.findByUserLogin(existingUserLogin);
		for (Token token : tokens) {
			tokenRepository.delete(token);
		}

		String encodedPassword = passwordEncoder.encode(newPassword);
		existingUserLogin.setPassword(encodedPassword);

		userLoginRepository.save(existingUserLogin);

	}

	@Override
	public UserLogin getUserLoginDetailsFromToken(String token) throws Exception {
		if (jwtTokenUtil.isTokenExpired(token)) {
			throw new ExpiredTokenException("Token is expired");
		}
		String username = jwtTokenUtil.extractUsername(token);
		Optional<UserLogin> userLogin = userLoginRepository.findByUsername(username);

		if (userLogin.isPresent()) {
			return userLogin.get();
		} else {
			throw new Exception("User not found");
		}
	}

	@Override
	public UserLogin getUserLoginDetailsFromRefreshToken(String refreshToken) throws Exception {
		Token existingToken = tokenRepository.findByRefreshToken(refreshToken);

		return getUserLoginDetailsFromToken(existingToken.getToken());
	}

}
