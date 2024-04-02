package com.example.Logistic_Web_App_Service_Login.services.role;

import com.example.Logistic_Web_App_Service_Login.models.Role;

public interface IRoleRedisService {
	void clear();

	Role getRoleById(Long roleId) throws Exception;

	void saveRoleById(Long roleId, Role role) throws Exception;
}
