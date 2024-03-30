package com.example.Logistic_Web_App_Service_Login.services.role;

import java.util.List;

import com.example.Logistic_Web_App_Service_Login.dtos.RoleDTO;
import com.example.Logistic_Web_App_Service_Login.models.Role;

public interface IRoleService {
	Role createRole(RoleDTO roleDTO);

	Role getRoleById(Long roleId);
	
	List<Role> getRoleByName(String name);
	
	Role updateRole(Long roleId, RoleDTO roleDTO);

	Role deleteRoleById(Long roleId) throws Exception;
}
