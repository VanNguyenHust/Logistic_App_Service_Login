package com.hust.globalict.main.controllers;

import com.hust.globalict.main.components.LocalizationUtils;
import com.hust.globalict.main.utils.MessageKeys;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.hust.globalict.main.dtos.RoleDTO;
import com.hust.globalict.main.constants.Uri;
import com.hust.globalict.main.modules.Role;
import com.hust.globalict.main.responses.RoleResponse;
import com.hust.globalict.main.services.role.IRoleService;
import com.hust.globalict.main.utils.ResponseObject;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = Uri.ROLE)
@Validated
@RequiredArgsConstructor
public class RoleController {
	private final IRoleService roleService;

	private final LocalizationUtils localizationUtils;

	@GetMapping()
	public ResponseEntity<ResponseObject> getRoleById(@RequestHeader(name = "role_id") Long roleId) throws Exception {
		Role existingRole = roleService.getRoleById(roleId);
		
		return ResponseEntity.ok(ResponseObject.builder()
				.status(HttpStatus.OK)
				.message(localizationUtils.getLocalizedMessage(MessageKeys.ROLE_GET_BY_ID_SUCCESSFULLY))
				.data(existingRole)
				.build());
	}

	@PostMapping("")
	@PreAuthorize("hasRole('ROLE_SYS-ADMIN')")
	public ResponseEntity<ResponseObject> createRole(@Valid @RequestBody RoleDTO roleDTO) throws Exception {
		Role newRole = Role.builder()
				.name(roleDTO.getName())
				.build();
		
		newRole = roleService.createRole(newRole);
		
		RoleResponse roleResponse = RoleResponse.builder()
				.id(newRole.getId())
				.name(newRole.getName())
				.build();

		return ResponseEntity.ok(ResponseObject.builder()
				.status(HttpStatus.CREATED)
				.message(localizationUtils.getLocalizedMessage(MessageKeys.ROLE_CREATE_SUCCESSFULLY))
				.data(roleResponse)
				.build());
		
	}

	@PutMapping("/{roleId}")
	@PreAuthorize("hasRole('ROLE_SYS-ADMIN')")
	public ResponseEntity<ResponseObject> updateRole(@RequestHeader(name = "role_id") Long roleId, @Valid @RequestBody RoleDTO roleDTO) throws Exception {
		Role existingRole = roleService.getRoleById(roleId);

		existingRole.setName(roleDTO.getName());
		roleService.updateRole(roleId, existingRole);

		RoleResponse roleResponse = RoleResponse.builder()
				.id(existingRole.getId())
				.name(existingRole.getName())
				.build();

		return ResponseEntity.ok(ResponseObject.builder()
				.status(HttpStatus.OK)
				.message(localizationUtils.getLocalizedMessage(MessageKeys.ROLE_UPDATE_SUCCESSFULLY))
				.data(roleResponse)
				.build());
	}

	@DeleteMapping("/{roleId}")
	@PreAuthorize("hasRole('ROLE_SYS-ADMIN')")
	public ResponseEntity<ResponseObject> deleteRole(@RequestHeader(name = "role_id") Long roleId) throws Exception {
		roleService.deleteRoleById(roleId);

		return ResponseEntity.ok(ResponseObject.builder()
				.status(HttpStatus.OK)
				.message(localizationUtils.getLocalizedMessage(MessageKeys.ROLE_DELETE_SUCCESSFULLY))
				.build());
	}
}
