package com.example.Logistic_Web_App_Service_Login.modules.role.service;

import java.util.List;

import com.example.Logistic_Web_App_Service_Login.common.entity.Role;
import com.example.Logistic_Web_App_Service_Login.modules.role.dto.RoleDTO;

public interface IRoleService {
	Role createRole(RoleDTO roleDTO);

	Role getRoleById(Long roleId);
	
	List<Role> getRoleByName(String name);
	
	Role updateRole(Long roleId, RoleDTO roleDTO);

	Role deleteRoleById(Long roleId) throws Exception;
}
