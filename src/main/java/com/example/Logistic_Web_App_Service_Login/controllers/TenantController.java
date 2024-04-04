package com.example.Logistic_Web_App_Service_Login.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Logistic_Web_App_Service_Login.components.LocalizationUtils;
import com.example.Logistic_Web_App_Service_Login.dtos.TenantDTO;
import com.example.Logistic_Web_App_Service_Login.enums.Uri;
import com.example.Logistic_Web_App_Service_Login.mappers.TenantMapper;
import com.example.Logistic_Web_App_Service_Login.models.Tenant;
import com.example.Logistic_Web_App_Service_Login.responses.TenantResponse;
import com.example.Logistic_Web_App_Service_Login.services.tenant.TenantRedisService;
import com.example.Logistic_Web_App_Service_Login.services.tenant.TenantService;
import com.example.Logistic_Web_App_Service_Login.utils.MessageKeys;
import com.example.Logistic_Web_App_Service_Login.utils.ResponseObject;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = Uri.TENANT)
@Validated
@RequiredArgsConstructor
public class TenantController {
	private final TenantService tenantService;
	private final TenantRedisService tenantRedisService;

	private final LocalizationUtils localizationUtils;

	@Autowired
	TenantMapper tenantMapper;

	@PostMapping()
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<ResponseObject> createTenant(@Valid @RequestBody TenantDTO tenantDTO) throws Exception {
		Tenant tenant = tenantService.createTenant(tenantDTO);

		TenantResponse tenantResponse = tenantMapper.mapToTenantResponse(tenant);

		tenantRedisService.saveTenantById(tenant.getId(), tenantResponse);

		return ResponseEntity
				.ok(ResponseObject.builder()
						.status(HttpStatus.OK)
						.message(localizationUtils.getLocalizedMessage(MessageKeys.TENANT_CREATE_SUCCESSFULLY))
						.data(tenantResponse)
						.build());
	}

	@GetMapping("/{tenantId}")
	public ResponseEntity<ResponseObject> getTenantById(@PathVariable Long tenantId) throws Exception {
		TenantResponse tenantResponse = tenantRedisService.getTenantById(tenantId);

		if (tenantResponse == null) {
			Tenant tenant = tenantService.getTenantById(tenantId);

			tenantResponse = tenantMapper.mapToTenantResponse(tenant);

			tenantRedisService.saveTenantById(tenantId, tenantResponse);
		}

		return ResponseEntity
				.ok(ResponseObject.builder()
						.status(HttpStatus.OK)
						.message(String.format(localizationUtils.getLocalizedMessage(MessageKeys.TENANT_GET_BY_ID_SUCCESSFULLY), tenantId))
						.data(tenantResponse)
						.build());
	}
	
	@PutMapping("/{tenantId}")
	public ResponseEntity<ResponseObject> updateTenant(@PathVariable Long tenantId, @Valid @RequestBody TenantDTO tenantDTO) throws Exception {
		TenantResponse tenantResponse = tenantRedisService.getTenantById(tenantId);
		
		if (tenantResponse == null) {
			Tenant existingTenant = tenantService.getTenantById(tenantId);
						
			tenantResponse = tenantMapper.mapToTenantResponse(existingTenant);
		}
		
		Tenant tenant = tenantMapper.mapToTenantEntity(tenantDTO);
		tenant.setId(tenantId);
		tenant.setCreatedAt(tenantResponse.getCreatedAt());
		tenant.setUpdatedAt(tenantResponse.getUpdatedAt());
		
		tenantResponse = tenantMapper.mapToTenantResponse(tenantService.updateTenant(tenantId, tenant));
		tenantRedisService.saveTenantById(tenantId, tenantResponse);
		
		return ResponseEntity.ok(ResponseObject.builder()
				.status(HttpStatus.OK)
				.message(localizationUtils.getLocalizedMessage(MessageKeys.TENANT_UPDATE_SUCCESSFULLY))
				.data(tenantResponse)
				.build());
	}
	
	@DeleteMapping("/{tenantId}")
	public ResponseEntity<ResponseObject> deleteTenantById(@PathVariable Long tenantId) throws Exception {
		TenantResponse tenantResponse = tenantRedisService.getTenantById(tenantId);
		
		if (tenantResponse == null) {
			Tenant existingTenant = tenantService.getTenantById(tenantId);
			
			tenantResponse = tenantMapper.mapToTenantResponse(existingTenant);
		}
		
		Tenant tenant = tenantMapper.mapToTenantEntity(tenantResponse);
		
		tenantService.deleteTenant(tenant);
		
		return ResponseEntity.ok(ResponseObject.builder()
				.status(HttpStatus.OK)
				.message(localizationUtils.getLocalizedMessage(MessageKeys.TENANT_DELETE_SUCCESSFULLY))
				.build());
	}
}
