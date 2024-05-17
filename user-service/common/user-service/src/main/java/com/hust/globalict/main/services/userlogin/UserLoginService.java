package com.hust.globalict.main.services.userlogin;

import java.util.List;
import java.util.Optional;

import com.hust.globalict.main.modules.User;
import com.hust.globalict.main.repositories.UserRepository;
import com.hust.globalict.main.services.user.IUserRedisService;
import com.hust.globalict.main.utils.MessageKeyExceptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hust.globalict.main.components.JwtTokenUtil;
import com.hust.globalict.main.components.LocalizationUtils;
import com.hust.globalict.main.constants.StatusRole;
import com.hust.globalict.main.exceptions.DataNotFoundException;
import com.hust.globalict.main.exceptions.ExpiredTokenException;
import com.hust.globalict.main.exceptions.InvalidPasswordException;
import com.hust.globalict.main.mappers.UserLoginMapper;
import com.hust.globalict.main.modules.Token;
import com.hust.globalict.main.modules.UserLogin;
import com.hust.globalict.main.repositories.TokenRepository;
import com.hust.globalict.main.repositories.UserLoginRepository;
import com.hust.globalict.main.utils.MessageKeys;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserLoginService implements IUserLoginService {
	private final UserLoginRepository userLoginRepository;
	private final IUserLoginRedisService userLoginRedisService;

	private final UserRepository userRepository;
	private final IUserRedisService userRedisService;

	private final AuthenticationManager authenticationManager;
	private final JwtTokenUtil jwtTokenUtil;
	private final TokenRepository tokenRepository;
	private final PasswordEncoder passwordEncoder;

	private final LocalizationUtils localizationUtils;

	@Autowired
	UserLoginMapper userLoginMapper;

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

		if (userLogin.getRole().getName().equalsIgnoreCase(StatusRole.ADMIN)) {
			throw new Exception(localizationUtils.getLocalizedMessage(MessageKeys.CREATE_USER_NOT_ADMIN));
		}

		if (userLogin.getUser() == null) {
			User user = new User();

			userRepository.save(user);
			userRedisService.saveUserById(user.getId(), user);
		}

		String encodedPassword = passwordEncoder.encode(userLogin.getPassword());
		userLogin.setPassword(encodedPassword);

		userLoginRepository.save(userLogin);
		userLoginRedisService.saveUserLoginById(userLogin.getId(), userLogin);

		return userLogin;
	}

	@Override
	public UserLogin getUserLoginById(Long userLoginId) throws Exception {
		UserLogin userLogin = userLoginRedisService.getUserLoginById(userLoginId);
		if (userLogin == null) {
			userLogin = userLoginRepository.findById(userLoginId).orElseThrow(() ->
					new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeyExceptions.USER_LOGIN_NOT_FOUND)));

			userLoginRedisService.saveUserLoginById(userLogin.getId(), userLogin);
		}

		return userLogin;
	}

	@Override
	public String login(String username, String password, String loginType) throws Exception {
		UserLogin existingUserLogin = userLoginRepository.findByUsernameAndLoginType(username, loginType);

		if (existingUserLogin == null) {
			throw new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeyExceptions.USER_LOGIN_INVALID_CREDENTIALS));
		}

		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,
				password);

		authenticationManager.authenticate(authenticationToken);

		return jwtTokenUtil.generateToken(existingUserLogin);
	}

	@Override
	@Transactional
	public void resetPassword(Long userLoginId, String newPassword)
            throws Exception {
		UserLogin existingUserLogin = getUserLoginById(userLoginId);

		// reset password => clear token
		List<Token> tokens = tokenRepository.findByUserLogin(existingUserLogin);
        tokenRepository.deleteAll(tokens);

		String encodedPassword = passwordEncoder.encode(newPassword);
		existingUserLogin.setPassword(encodedPassword);

		userLoginRepository.save(existingUserLogin);
		userLoginRedisService.saveUserLoginById(existingUserLogin.getId(), existingUserLogin);
		userLoginRedisService.saveUserLoginByUsername(existingUserLogin.getUsername(), existingUserLogin);
	}

	@Override
	public UserLogin getUserLoginDetailsFromToken(String token, String tokenType) throws Exception {
		if (jwtTokenUtil.isTokenExpired(token)) {
			throw new ExpiredTokenException(localizationUtils.getLocalizedMessage(MessageKeyExceptions.TOKEN_IS_EXPIRED));
		}

		String username = jwtTokenUtil.extractUsername(token);
		UserLogin userLogin = userLoginRepository.findByUsername(username);

		if (userLogin == null) {
			throw new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeyExceptions.USER_LOGIN_NOT_FOUND));
		} else {
			userLoginRedisService.saveUserLoginByTokenOrRefreshToken(token, tokenType, userLogin);
			return userLogin;
		}
	}

	@Override
	public UserLogin getUserLoginDetailsFromRefreshToken(String refreshToken, String tokenType) throws Exception {
		Token existingToken = tokenRepository.findByRefreshToken(refreshToken);

        return getUserLoginDetailsFromToken(existingToken.getToken(), tokenType);
	}

}

