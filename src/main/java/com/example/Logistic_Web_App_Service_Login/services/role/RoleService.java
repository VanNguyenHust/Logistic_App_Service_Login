package com.example.Logistic_Web_App_Service_Login.services.role;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.Logistic_Web_App_Service_Login.dtos.RoleDTO;
import com.example.Logistic_Web_App_Service_Login.models.Role;
import com.example.Logistic_Web_App_Service_Login.models.UserLogin;
import com.example.Logistic_Web_App_Service_Login.repositories.RoleRepository;
import com.example.Logistic_Web_App_Service_Login.repositories.UserLoginRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleService implements IRoleService {
	private final RoleRepository roleRepository;
	private final UserLoginRepository userLoginRepository;

	@Override
	@Transactional
	public Role createRole(RoleDTO roleDTO) {
		Role newRole = Role.builder().name(roleDTO.getName()).build();

		return roleRepository.save(newRole);
	}

	@Override
	public Role getRoleById(Long roleId) {
		return roleRepository.findById(roleId).orElseThrow(() -> new RuntimeException("Role not found"));
	}

	@Override
	@Transactional
	public Role updateRole(Long roleId, RoleDTO roleDTO) {
		Role existingRole = getRoleById(roleId);

		existingRole.setName(roleDTO.getName());
		roleRepository.save(existingRole);

		return existingRole;
	}

	@Override
	@Transactional
	public Role deleteRoleById(Long roleId) throws Exception {
		Role role = getRoleById(roleId);
		
		Set<UserLogin> userLogins = userLoginRepository.findByRole(role);
		
		if (!userLogins.isEmpty()) {
			throw new IllegalStateException("Cannot delete role with associated user");
		} else {
			roleRepository.deleteById(roleId);
			return role;
		}
	}

	@Override
	public List<Role> getRoleByName(String name) {
		List<Role> roles = roleRepository.findByName(name);
		
		if (roles.isEmpty()) {
			throw new RuntimeException("Role name not found");
		} 
		
		return roles;
	}

}
