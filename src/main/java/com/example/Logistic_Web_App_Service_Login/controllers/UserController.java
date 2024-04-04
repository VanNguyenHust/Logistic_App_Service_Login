package com.example.Logistic_Web_App_Service_Login.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Logistic_Web_App_Service_Login.components.LocalizationUtils;
import com.example.Logistic_Web_App_Service_Login.dtos.UserDTO;
import com.example.Logistic_Web_App_Service_Login.enums.Uri;
import com.example.Logistic_Web_App_Service_Login.mappers.UserMapper;
import com.example.Logistic_Web_App_Service_Login.models.User;
import com.example.Logistic_Web_App_Service_Login.responses.UserResponse;
import com.example.Logistic_Web_App_Service_Login.services.user.UserRedisService;
import com.example.Logistic_Web_App_Service_Login.services.user.UserService;
import com.example.Logistic_Web_App_Service_Login.utils.MessageKeys;
import com.example.Logistic_Web_App_Service_Login.utils.ResponseObject;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = Uri.USER)
public class UserController {
	private final UserRedisService userRedisService;
	private final UserService userService;

	private final LocalizationUtils localizationUtils;
	
	@Autowired
	UserMapper userMapper;

	@PostMapping()
	public ResponseEntity<ResponseObject> createUser(@Valid @RequestBody UserDTO userDTO, BindingResult result)
			throws Exception {
		if (result.hasErrors()) {
			List<String> errorMessages = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();

			return ResponseEntity.badRequest()
					.body(ResponseObject.builder()
							.status(HttpStatus.BAD_REQUEST)
							.data(null)
							.message(errorMessages.toString())
							.build());
		}

		User user = userService.createUser(userDTO);

		UserResponse userResponse = userMapper.mapToUserReponse(user);

		return ResponseEntity.created(null).body(ResponseObject.builder()
				.status(HttpStatus.CREATED)
				.data(userResponse)
				.message(localizationUtils.getLocalizedMessage(MessageKeys.CREATE_USER_SUCCESSFULLY))
				.build());
	}

	@GetMapping("/all/{keyword}")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<ResponseObject> getAllUserByKeyword(@PathVariable String keyword,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "3") int limit) throws Exception {
		// Tạo Pageable từ thông tin trang và giới hạn
		PageRequest pageRequest = PageRequest.of(page, limit,
				// Sort.by("createdAt").descending()
				Sort.by("id").ascending());

		List<UserResponse> userResponses = userRedisService.getAllUserByKeyword(keyword, pageRequest);

		if (userResponses == null) {
			Page<UserResponse> userPage = userService.getAllUserByKeyword(keyword, pageRequest);
			
			userResponses = userPage.getContent();
			
			userRedisService.saveAllUserByKeyword(userResponses, keyword, pageRequest);
		}
		
		return ResponseEntity.ok(ResponseObject.builder()
				.status(HttpStatus.OK)
				.message(localizationUtils.getLocalizedMessage(MessageKeys.GET_ALL_USER_CONTAIN_KEYWORD_SUCCESSFULLY))
				.data(userResponses)
				.build());
	}
}
