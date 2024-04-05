package com.example.Logistic_Web_App_Service_Login.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Logistic_Web_App_Service_Login.dtos.RoleDTO;
import com.example.Logistic_Web_App_Service_Login.enums.Uri;
import com.example.Logistic_Web_App_Service_Login.models.Role;
import com.example.Logistic_Web_App_Service_Login.responses.RoleResponse;
import com.example.Logistic_Web_App_Service_Login.services.role.RoleService;
import com.example.Logistic_Web_App_Service_Login.utils.ResponseObject;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = Uri.ROLE)
@Validated
@RequiredArgsConstructor
public class RoleController {
	private final RoleService roleService;
//	private final LocalizationUtils localizationUtils;

	@GetMapping("/{id}")
	public ResponseEntity<ResponseObject> getRoleById(@PathVariable Long id) {
		Role existingRole = roleService.getRoleById(id);
		return ResponseEntity.ok(ResponseObject.builder().data(existingRole)
				.message("Get role information successfully").status(HttpStatus.OK).build());
	}

	@PostMapping("")
	@PreAuthorize("hasRole('ROLE_SYS-ADMIN')")
	public ResponseEntity<RoleResponse> createRole(@Valid @RequestBody RoleDTO roleDTO, BindingResult result) {
		RoleResponse roleResponse = new RoleResponse();

		if (result.hasErrors()) {
			List<String> errorMessages = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();

//			roleResponse.setMessage(localizationUtils.getLocalizedMessage(MessageKeys.INSERT_ROLE_SUCCESSFULLY));
			roleResponse.setErrors(errorMessages);

			return ResponseEntity.badRequest().body(roleResponse);
		}

		Role role = roleService.createRole(roleDTO);
		roleResponse.setRole(role);
		return ResponseEntity.created(null).body(roleResponse);
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ROLE_SYS-ADMIN')")
	public ResponseEntity<ResponseObject> updateRole(@PathVariable Long id, @Valid @RequestBody RoleDTO roleDTO) {
		roleService.updateRole(id, roleDTO);
		return ResponseEntity.ok(ResponseObject.builder().data(roleService.getRoleById(id))
//				.message(localizationUtils.getLocalizedMessage(MessageKeys.UPDATE_ROLE_SUCCESSFULLY))
				.build());
	}

//	@DeleteMapping("/{id}")
//	@PreAuthorize("hasRole('ROLE_SYS-ADMIN')")
//	public ResponseEntity<ResponseObject> deleteRole(@PathVariable Long id) throws Exception {
//		roleService.deleteRoleById(id);
//		return ResponseEntity
//				.ok(ResponseObject.builder().status(HttpStatus.OK).message("Delete role successfully").build());
//	}
}
