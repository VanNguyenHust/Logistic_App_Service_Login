package com.example.Logistic_Web_App_Service_Login.modules.userlogin.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Logistic_Web_App_Service_Login.common.components.LocalizationUtils;
import com.example.Logistic_Web_App_Service_Login.common.entity.UserLogin;
import com.example.Logistic_Web_App_Service_Login.common.enums.Uri;
import com.example.Logistic_Web_App_Service_Login.common.utils.MessageKeys;
import com.example.Logistic_Web_App_Service_Login.common.utils.ResponseObject;
import com.example.Logistic_Web_App_Service_Login.modules.userlogin.dto.UserLoginDTO;
import com.example.Logistic_Web_App_Service_Login.modules.userlogin.mapper.UserLoginMapper;
import com.example.Logistic_Web_App_Service_Login.modules.userlogin.response.UserLoginResponse;
import com.example.Logistic_Web_App_Service_Login.modules.userlogin.service.UserLoginService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = Uri.USER_LOGIN)
public class UserLoginController {
	private final UserLoginService userLoginService;
	
	private final LocalizationUtils localizationUtils;
	
	@Autowired
	UserLoginMapper userLoginMapper;
	
	@PostMapping()
	public ResponseEntity<ResponseObject> createUserLogin(@Valid @RequestBody UserLoginDTO userLoginDTO) throws Exception{		
		if (!userLoginDTO.getPassword().equals(userLoginDTO.getRetypePassword())) {
			return ResponseEntity.badRequest().body(ResponseObject.builder().status(HttpStatus.BAD_REQUEST).data(null)
					.message(localizationUtils.getLocalizedMessage(MessageKeys.PASSWORD_NOT_MATCH)).build());
		}
		
		UserLogin userLogin = userLoginService.createUserLogin(userLoginDTO);
		
		UserLoginResponse userLoginResponse = userLoginMapper.mapTopUserLoginResponse(userLogin);
		userLoginResponse.setRoleName(userLogin.getRole().getName());
		
		return ResponseEntity.ok(ResponseObject.builder()
				.message("create success")
				.status(HttpStatus.OK)
				.data(userLoginResponse)
				.build());
	}
}
