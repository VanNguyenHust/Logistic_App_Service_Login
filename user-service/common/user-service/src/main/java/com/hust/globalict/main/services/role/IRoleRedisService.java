package com.hust.globalict.main.services.role;

import com.hust.globalict.main.modules.Role;

public interface IRoleRedisService {
	void clear();

	Role getRoleById(Long roleId) throws Exception;

	void saveRoleById(Long roleId, Role role) throws Exception;

	Role getRoleByName(String roleName) throws Exception;

	void saveRoleByName(String roleName, Role role) throws Exception;
}
