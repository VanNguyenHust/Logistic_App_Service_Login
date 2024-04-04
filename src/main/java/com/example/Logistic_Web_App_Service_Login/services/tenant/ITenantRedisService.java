package com.example.Logistic_Web_App_Service_Login.services.tenant;

import com.example.Logistic_Web_App_Service_Login.responses.TenantResponse;

public interface ITenantRedisService {
	void clear(Long tenantId);
	
	TenantResponse getTenantById(Long tenantId) throws Exception;
	
	void saveTenantById(Long tenantId, TenantResponse tenantResponse) throws Exception;
}
