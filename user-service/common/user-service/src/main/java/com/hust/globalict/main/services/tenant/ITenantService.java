package com.hust.globalict.main.services.tenant;

import com.hust.globalict.main.modules.Tenant;

public interface ITenantService {
	Tenant createTenant(Tenant tenant) throws Exception;
	
	Tenant getTenantById(Long tenantId) throws Exception;
	
	Tenant updateTenant(Long tenantId, Tenant tenant) throws Exception;
	
	void deleteTenant(Long tenantId) throws Exception;
}
