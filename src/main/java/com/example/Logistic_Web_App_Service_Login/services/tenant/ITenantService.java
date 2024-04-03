package com.example.Logistic_Web_App_Service_Login.services.tenant;

import com.example.Logistic_Web_App_Service_Login.dtos.TenantDTO;
import com.example.Logistic_Web_App_Service_Login.exceptions.DataNotFoundException;
import com.example.Logistic_Web_App_Service_Login.models.Tenant;

public interface ITenantService {
	Tenant createTenant(TenantDTO tenantDTO);
	
	Tenant getTenantById(Long tenantId) throws DataNotFoundException;
}
