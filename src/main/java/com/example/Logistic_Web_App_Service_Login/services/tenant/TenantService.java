package com.example.Logistic_Web_App_Service_Login.services.tenant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Logistic_Web_App_Service_Login.components.LocalizationUtils;
import com.example.Logistic_Web_App_Service_Login.dtos.TenantDTO;
import com.example.Logistic_Web_App_Service_Login.exceptions.DataNotFoundException;
import com.example.Logistic_Web_App_Service_Login.mappers.TenantMapper;
import com.example.Logistic_Web_App_Service_Login.models.Tenant;
import com.example.Logistic_Web_App_Service_Login.repositories.TenantRepository;
import com.example.Logistic_Web_App_Service_Login.utils.MessageKeys;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TenantService implements ITenantService {
	private final TenantRepository tenantRepository;

	private LocalizationUtils localizationUtils;

	@Autowired
	TenantMapper tenantMapper;

	@Override
	@Transactional
	public Tenant createTenant(TenantDTO tenantDTO) {
		Tenant newTenant = tenantMapper.mapToTenantEntity(tenantDTO);

		return tenantRepository.save(newTenant);
	}

	@Override
	public Tenant getTenantById(Long tenantId) throws DataNotFoundException {
		return tenantRepository.findById(tenantId).orElseThrow(() -> new DataNotFoundException(String
				.format(localizationUtils.getLocalizedMessage(MessageKeys.TENANT_GET_BY_ID_NOT_FOUND), tenantId)));
	}

}
