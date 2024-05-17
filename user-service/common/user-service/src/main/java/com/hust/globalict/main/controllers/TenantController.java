package com.hust.globalict.main.controllers;

import com.hust.globalict.main.services.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.hust.globalict.main.components.LocalizationUtils;
import com.hust.globalict.main.dtos.TenantDTO;
import com.hust.globalict.main.constants.Uri;
import com.hust.globalict.main.mappers.TenantMapper;
import com.hust.globalict.main.modules.Tenant;
import com.hust.globalict.main.responses.TenantResponse;
import com.hust.globalict.main.services.tenant.ITenantService;
import com.hust.globalict.main.utils.MessageKeys;
import com.hust.globalict.main.utils.ResponseObject;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = Uri.TENANT)
@Validated
@RequiredArgsConstructor
public class TenantController {
    private final ITenantService tenantService;
    private final TenantMapper tenantMapper;
    private final IUserService userService;

    private final LocalizationUtils localizationUtils;

    @PostMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<ResponseObject> createTenant(@Valid @RequestBody TenantDTO tenantDTO) throws Exception {
        Tenant tenant = tenantMapper.mapToTenantEntity(tenantDTO);
        if (tenantDTO.getUserId() != null) {
            tenant.setUser(userService.getUserById(tenantDTO.getUserId()));
        }

        tenant = tenantService.createTenant(tenant);

        TenantResponse tenantResponse = tenantMapper.mapToTenantResponse(tenant);

        return ResponseEntity.ok(ResponseObject.builder()
                .status(HttpStatus.OK)
                .message(localizationUtils.getLocalizedMessage(MessageKeys.TENANT_CREATE_SUCCESSFULLY))
                .data(tenantResponse)
                .build());
    }

    @GetMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_SYS-ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<ResponseObject> getTenantById(@RequestHeader(name = "tenant_id") Long tenantId) throws Exception {
        Tenant tenant = tenantService.getTenantById(tenantId);

        TenantResponse tenantResponse = tenantMapper.mapToTenantResponse(tenant);

        return ResponseEntity.ok(ResponseObject.builder()
                .status(HttpStatus.OK)
                .message(String.format(localizationUtils.getLocalizedMessage(MessageKeys.TENANT_GET_BY_ID_SUCCESSFULLY), tenantId))
                .data(tenantResponse)
                .build());
    }

    @PutMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<ResponseObject> updateTenant(@RequestHeader(name = "tenant_id") Long tenantId,
                                                       @Valid @RequestBody TenantDTO tenantDTO) throws Exception {
        Tenant tenant = tenantService.getTenantById(tenantId);

        Tenant tenantUpdate = tenantMapper.mapToTenantEntity(tenantDTO);
        tenantUpdate.setId(tenantId);
        tenantUpdate.setCreatedAt(tenant.getCreatedAt());

        tenantUpdate = tenantService.updateTenant(tenantId, tenantUpdate);

        TenantResponse tenantResponse = tenantMapper.mapToTenantResponse(tenantUpdate);

        return ResponseEntity.ok(ResponseObject.builder()
                .status(HttpStatus.OK)
                .message(localizationUtils.getLocalizedMessage(MessageKeys.TENANT_UPDATE_SUCCESSFULLY))
                .data(tenantResponse)
                .build());
    }

    @DeleteMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<ResponseObject> deleteTenantById(@RequestHeader(name = "tenant_id") Long tenantId) throws Exception {
        tenantService.deleteTenant(tenantId);

        return ResponseEntity.ok(ResponseObject.builder()
                .status(HttpStatus.OK)
                .message(localizationUtils.getLocalizedMessage(MessageKeys.TENANT_DELETE_SUCCESSFULLY))
                .build());
    }
}
