package com.example.Logistic_Web_App_Service_Login.modules.user.api;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Logistic_Web_App_Service_Login.common.components.LocalizationUtils;
import com.example.Logistic_Web_App_Service_Login.common.entity.User;
import com.example.Logistic_Web_App_Service_Login.common.enums.Uri;
import com.example.Logistic_Web_App_Service_Login.common.utils.ResponseObject;
import com.example.Logistic_Web_App_Service_Login.modules.role.service.RoleService;
import com.example.Logistic_Web_App_Service_Login.modules.token.service.TokenService;
import com.example.Logistic_Web_App_Service_Login.modules.user.dto.UserDTO;
import com.example.Logistic_Web_App_Service_Login.modules.user.response.UserResponse;
import com.example.Logistic_Web_App_Service_Login.modules.user.service.UserService;
import com.example.Logistic_Web_App_Service_Login.modules.userlogin.service.UserLoginService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = Uri.USER)
public class UserController {
	private final UserService userService;
	
	private final LocalizationUtils localizationUtils;

	@PostMapping()
	public ResponseEntity<ResponseObject> createUser(@Valid @RequestBody UserDTO userDTO, BindingResult result) throws Exception {
		if (result.hasErrors()) {
			List<String> errorMessages = result.getFieldErrors()
					.stream()
					.map(FieldError::getDefaultMessage)
					.toList();

			return ResponseEntity.badRequest()
					.body(ResponseObject.builder()
							.status(HttpStatus.BAD_REQUEST)
							.data(null)
							.message(errorMessages.toString())
							.build());
		}

		User user = userService.createUser(userDTO);

		UserResponse userResponse = UserResponse.builder()
				.message("create user success")
				.user(user)
				.build();
		
		return ResponseEntity.created(null)
				.body(ResponseObject.builder()
						.status(HttpStatus.CREATED)
						.data(userResponse)
						.message("Đăng ký tài khoản thành công")
						.build());
	}
}
