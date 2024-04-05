package com.example.Logistic_Web_App_Service_Login.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Logistic_Web_App_Service_Login.components.LocalizationUtils;
import com.example.Logistic_Web_App_Service_Login.dtos.ChangePasswordDTO;
import com.example.Logistic_Web_App_Service_Login.dtos.RefreshTokenDTO;
import com.example.Logistic_Web_App_Service_Login.dtos.UserLoginDTO;
import com.example.Logistic_Web_App_Service_Login.enums.Uri;
import com.example.Logistic_Web_App_Service_Login.exceptions.DataNotFoundException;
import com.example.Logistic_Web_App_Service_Login.exceptions.InvalidPasswordException;
import com.example.Logistic_Web_App_Service_Login.mappers.UserLoginMapper;
import com.example.Logistic_Web_App_Service_Login.models.Role;
import com.example.Logistic_Web_App_Service_Login.models.Token;
import com.example.Logistic_Web_App_Service_Login.models.User;
import com.example.Logistic_Web_App_Service_Login.models.UserLogin;
import com.example.Logistic_Web_App_Service_Login.responses.LoginResponse;
import com.example.Logistic_Web_App_Service_Login.responses.UserLoginResponse;
import com.example.Logistic_Web_App_Service_Login.services.role.RoleRedisService;
import com.example.Logistic_Web_App_Service_Login.services.role.RoleService;
import com.example.Logistic_Web_App_Service_Login.services.token.TokenService;
import com.example.Logistic_Web_App_Service_Login.services.user.UserRedisService;
import com.example.Logistic_Web_App_Service_Login.services.user.UserService;
import com.example.Logistic_Web_App_Service_Login.services.userlogin.UserLoginRedisService;
import com.example.Logistic_Web_App_Service_Login.services.userlogin.UserLoginService;
import com.example.Logistic_Web_App_Service_Login.utils.MessageKeys;
import com.example.Logistic_Web_App_Service_Login.utils.ResponseObject;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = Uri.USER_LOGIN)
public class UserLoginController {
	private final UserRedisService userRedisService;
	private final UserService userService;
	private final RoleService roleService;
	private final RoleRedisService roleRedisService;

	private final UserLoginService userLoginService;
	private final UserLoginRedisService userLoginRedisService;

	private final TokenService tokenService;
	private final AuthenticationManager authenticationManager;

	private final LocalizationUtils localizationUtils;

	@Autowired
	UserLoginMapper userLoginMapper;

	@PostMapping("/{register}")
	public ResponseEntity<ResponseObject> createUserLogin(@Valid @RequestBody UserLoginDTO userLoginDTO)
			throws Exception {
		if (!userLoginDTO.getPassword().equals(userLoginDTO.getRetypePassword())) {
			throw new BadCredentialsException(localizationUtils.getLocalizedMessage(MessageKeys.USER_LOGIN_PASSWORD_NOT_MATCH));
		}
		
		Long userId = userLoginDTO.getUserId();
		User user = userRedisService.getUserById(userId);
		if (user == null) {
			user = userService.getUserById(userId);

			userRedisService.saveUserById(userId, user);
		}
		
		Long roleId = userLoginDTO.getRoleId();
		Role role = roleRedisService.getRoleById(roleId);
		if (role == null) {
			role = roleService.getRoleById(roleId);

			roleRedisService.saveRoleById(roleId, role);
		}
		
		UserLogin userLogin = userLoginMapper.mapToUserLoginEntity(userLoginDTO);
		userLogin.setRole(role);
		userLogin.setUser(user);

		UserLogin newUserLogin = userLoginService.createUserLogin(userLogin);

		UserLoginResponse userLoginResponse = userLoginMapper.mapTopUserLoginResponse(newUserLogin);

		userLoginRedisService.saveUserLoginByUsername(userLoginDTO.getUsername(), userLoginResponse);

		return ResponseEntity.ok(ResponseObject.builder()
				.message(localizationUtils.getLocalizedMessage(MessageKeys.USER_LOGIN_CREATE_SUCCESSFULLY))
				.status(HttpStatus.OK)
				.data(userLoginResponse)
				.build());
	}

	private boolean isMobileDevice(String userLoginAgent) {
		// Kiểm tra User-Agent header để xác định thiết bị di động
		// Ví dụ đơn giản:
		return userLoginAgent.toLowerCase().contains("mobile");
	}

	@PostMapping("/login")
	public ResponseEntity<ResponseObject> login(@Valid @RequestBody UserLoginDTO userLoginDTO,
			HttpServletRequest request) throws Exception {
		// Kiểm tra thông tin đăng nhập và sinh token
		String token = userLoginService.login(userLoginDTO.getUsername(), userLoginDTO.getPassword(),
				userLoginDTO.getLoginType());

		String userLoginAgent = request.getHeader("User-Agent");
		UserLogin userLoginDetail = userLoginService.getUserLoginDetailsFromToken(token);
		Token jwtToken = tokenService.addToken(userLoginDetail, token, isMobileDevice(userLoginAgent));

		userLoginRedisService.saveToken(jwtToken, userLoginDetail);

		LoginResponse loginResponse = LoginResponse.builder().token(jwtToken.getToken())
				.tokenType(jwtToken.getTokenType()).refreshToken(jwtToken.getRefreshToken())
				.userName(userLoginDetail.getUsername())
				.roles(userLoginDetail.getAuthorities().stream().map(item -> item.getAuthority()).toList())
				.id(jwtToken.getId()).build();

		return ResponseEntity.ok()
				.body(ResponseObject.builder()
						.message(localizationUtils.getLocalizedMessage(MessageKeys.USER_LOGIN_LOGIN_SUCCESSFULLY))
						.data(loginResponse)
						.status(HttpStatus.OK)
						.build());
	}

	@PostMapping("/refresh-token")
	public ResponseEntity<ResponseObject> refreshToken(@Valid @RequestBody RefreshTokenDTO refreshTokenDTO)
			throws Exception {
		UserLogin userLoginDetail = userLoginRedisService
				.getUserLoginDetailsFromRefreshToken(refreshTokenDTO.getRefreshToken(), refreshTokenDTO.getTokenType());

		if (userLoginDetail == null) {
			userLoginDetail = userLoginService.getUserLoginDetailsFromRefreshToken(refreshTokenDTO.getRefreshToken());
		}

		Token jwtToken = tokenService.refreshToken(refreshTokenDTO.getRefreshToken(), userLoginDetail);

		userLoginRedisService.clear();
		userLoginRedisService.saveToken(jwtToken, userLoginDetail);

		LoginResponse loginResponse = LoginResponse.builder().token(jwtToken.getToken())
				.tokenType(jwtToken.getTokenType()).refreshToken(jwtToken.getRefreshToken())
				.userName(userLoginDetail.getUsername())
				.roles(userLoginDetail.getAuthorities().stream().map(item -> item.getAuthority()).toList())
				.id(jwtToken.getId()).build();

		return ResponseEntity.ok()
				.body(ResponseObject.builder()
						.data(loginResponse)
						.message(localizationUtils.getLocalizedMessage(MessageKeys.REFRESH_TOKEN_SUCCESSFULLY))
						.status(HttpStatus.OK)
						.build());
	}

	@PutMapping("/reset-password/{userLoginId}")
	public ResponseEntity<ResponseObject> resetPassword(@Valid @PathVariable long userLoginId)
			throws DataNotFoundException {
		try {
			String newPassword = UUID.randomUUID().toString().substring(0, 5);
			userLoginService.resetPassword(userLoginId, newPassword);

			return ResponseEntity.ok(ResponseObject.builder()
					.message(localizationUtils.getLocalizedMessage(MessageKeys.USER_LOGIN_RESET_PASSWORD_SUCCESSFULLY))
					.data(newPassword)
					.status(HttpStatus.OK)
					.build());

		} catch (InvalidPasswordException e) {
			return ResponseEntity.ok(ResponseObject.builder().message("Invalid password").data("")
					.status(HttpStatus.BAD_REQUEST).build());
		} catch (DataNotFoundException e) {
			throw new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.USER_LOGIN_NOT_FOUND));
		}
	}

	@PutMapping("/change-password/{userLoginId}")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<ResponseObject> changePassword(@Valid @PathVariable long userLoginId,
			@Valid @RequestBody ChangePasswordDTO changePasswordDTO) {
		try {
			UserLogin existingUserLogin = userLoginService.getUserLoginById(userLoginId);

			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
					existingUserLogin.getUsername(), changePasswordDTO.getOldPassword());

			authenticationManager.authenticate(authenticationToken);

			String newPassword = changePasswordDTO.getNewPassword();

			userLoginService.resetPassword(userLoginId, newPassword);

			return ResponseEntity.ok(ResponseObject.builder()
					.message(localizationUtils.getLocalizedMessage(MessageKeys.USER_LOGIN_CHANGE_PASSWORD_SUCCESSFULLY))
					.data(newPassword)
					.status(HttpStatus.OK)
					.build());
		} catch (InvalidPasswordException e) {
			return ResponseEntity.ok(ResponseObject
					.builder()
					.message("Invalid password")
					.data("")
					.status(HttpStatus.BAD_REQUEST)
					.build());
		} catch (DataNotFoundException e) {
			return ResponseEntity.ok(
					ResponseObject.builder()
					.message(localizationUtils.getLocalizedMessage(MessageKeys.USER_LOGIN_NOT_FOUND))
					.data("")
					.status(HttpStatus.BAD_REQUEST).build());
		}
	}
}
