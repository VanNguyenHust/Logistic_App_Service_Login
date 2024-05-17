package com.hust.globalict.main.services.tenant;

import com.hust.globalict.main.modules.Tenant;

public interface ITenantRedisService {
	void clear(Long tenantId);
	
	Tenant getTenantById(Long tenantId) throws Exception;
	
	void saveTenantById(Long tenantId, Tenant tenant) throws Exception;
}
