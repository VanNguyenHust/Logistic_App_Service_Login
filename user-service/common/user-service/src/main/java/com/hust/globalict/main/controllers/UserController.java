package com.hust.globalict.main.controllers;

import java.util.ArrayList;
import java.util.List;

import com.hust.globalict.main.modules.Department;
import com.hust.globalict.main.services.department.IDepartmentService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.hust.globalict.main.components.LocalizationUtils;
import com.hust.globalict.main.dtos.UserDTO;
import com.hust.globalict.main.constants.Uri;
import com.hust.globalict.main.mappers.UserMapper;
import com.hust.globalict.main.modules.Tenant;
import com.hust.globalict.main.modules.User;
import com.hust.globalict.main.responses.UserResponse;
import com.hust.globalict.main.services.tenant.ITenantService;
import com.hust.globalict.main.services.user.IUserService;
import com.hust.globalict.main.utils.MessageKeys;
import com.hust.globalict.main.utils.ResponseObject;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = Uri.USER)
public class UserController {
	private final IUserService userService;
	private final UserMapper userMapper;

	private final ITenantService tenantService;
	private final IDepartmentService departmentService;

	private final LocalizationUtils localizationUtils;

	@PostMapping()
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<ResponseObject> createUser(@Valid @RequestBody UserDTO userDTO)
			throws Exception {
		Tenant tenant = tenantService.getTenantById(userDTO.getTenantId());

		Department department = departmentService.getDepartmentById(userDTO.getDepartmentId());
		
		User newUser = userMapper.mapToUserEntity(userDTO);
		newUser.setTenant(tenant);
		newUser.setDepartment(department);
		
		UserResponse userResponse = userMapper.mapToUserResponse(userService.createUser(newUser));
		
		return ResponseEntity.ok().body(ResponseObject.builder()
				.status(HttpStatus.CREATED)
				.data(userResponse)
				.message(localizationUtils.getLocalizedMessage(MessageKeys.CREATE_USER_SUCCESSFULLY))
				.build());
	}

	@GetMapping("/all")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<ResponseObject> getAllUserByKeyword(@RequestHeader String keyword,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "3") int limit) throws Exception {
		// Tạo Pageable từ thông tin trang và giới hạn
		PageRequest pageRequest = PageRequest.of(page, limit,
				// Sort.by("createdAt").descending()
				Sort.by("id").ascending());

		List<User> users = userService.getAllUserByKeyword(keyword, pageRequest);
		
		List<UserResponse> userResponses = new ArrayList<>();
		for (User user : users) {
			userResponses.add(userMapper.mapToUserResponse(user));
		}
		
		return ResponseEntity.ok(ResponseObject.builder()
				.status(HttpStatus.OK)
				.message(localizationUtils.getLocalizedMessage(MessageKeys.GET_ALL_USER_CONTAIN_KEYWORD_SUCCESSFULLY))
				.data(userResponses)
				.build());
	}
	
	@PutMapping()
	@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
	public ResponseEntity<ResponseObject> updateUser(@RequestHeader(name = "user_id") Long userId, @RequestBody UserDTO userDTO) throws Exception {
		User user = userService.getUserById(userId);
		
		User userUpdate = userMapper.mapToUserEntity(userDTO);
		userUpdate.setId(userId);
		userUpdate.setCreatedAt(user.getCreatedAt());
		userUpdate.setTenant(user.getTenant());
		userUpdate.setDepartment(user.getDepartment());
		userUpdate.setIsActive(user.getIsActive());

		UserResponse userResponse = userMapper.mapToUserResponse(userService.updateUser(userUpdate));
		
		return ResponseEntity.ok(ResponseObject.builder()
				.status(HttpStatus.OK)
				.message(localizationUtils.getLocalizedMessage(MessageKeys.USER_UPDATE_SUCCESSFULLY))
				.data(userResponse)
				.build());
	}
}
