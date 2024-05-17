package com.hust.globalict.main.services.role;

import java.util.List;

import com.hust.globalict.main.dtos.RoleDTO;
import com.hust.globalict.main.modules.Role;

public interface IRoleService {
	Role createRole(Role role) throws Exception;

	Role getRoleById(Long roleId) throws Exception;
	
	Role getRoleByName(String name) throws Exception;
	
	Role updateRole(Long roleId, Role role) throws Exception;

	void deleteRoleById(Long roleId) throws Exception;
}
