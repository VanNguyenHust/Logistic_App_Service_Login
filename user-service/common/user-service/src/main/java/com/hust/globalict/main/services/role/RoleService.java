package com.hust.globalict.main.services.role;

import java.util.List;
import java.util.Set;

import com.hust.globalict.main.components.LocalizationUtils;
import com.hust.globalict.main.utils.MessageKeyExceptions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hust.globalict.main.dtos.RoleDTO;
import com.hust.globalict.main.exceptions.DataNotFoundException;
import com.hust.globalict.main.modules.Role;
import com.hust.globalict.main.modules.UserLogin;
import com.hust.globalict.main.repositories.RoleRepository;
import com.hust.globalict.main.repositories.UserLoginRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleService implements IRoleService {
	private final RoleRepository roleRepository;
	private final IRoleRedisService roleRedisService;
	private final UserLoginRepository userLoginRepository;

	private final LocalizationUtils localizationUtils;

	@Override
	@Transactional
	public Role createRole(Role role) throws Exception {
		if (roleRepository.existsByName(role.getName())) {
			throw new IllegalStateException(localizationUtils.getLocalizedMessage(MessageKeyExceptions.ROLE_NAME_EXISTED));
		}

		roleRepository.save(role);
		roleRedisService.saveRoleById(role.getId(), role);
		roleRedisService.saveRoleByName(role.getName(), role);

		return role;
	}

	@Override
	public Role getRoleById(Long roleId) throws Exception {
		Role role = roleRedisService.getRoleById(roleId);
		if (role == null) {
			role = roleRepository.findById(roleId).orElseThrow(() ->
					new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeyExceptions.ROLE_NOT_FOUND)));

			roleRedisService.saveRoleById(roleId, role);
		}

		return role;
	}

	@Override
	@Transactional
	public Role updateRole(Long roleId, Role role) throws Exception {
		Role existingRole = getRoleById(roleId);

		existingRole.setName(role.getName());
		roleRepository.save(existingRole);

		roleRedisService.saveRoleById(roleId, role);
		roleRedisService.saveRoleByName(role.getName(), role);

		return existingRole;
	}

	@Override
	@Transactional
	public void deleteRoleById(Long roleId) throws Exception {
		Role role = getRoleById(roleId);

		Set<UserLogin> userLogins = userLoginRepository.findByRole(role);

		if (!userLogins.isEmpty()) {
			throw new IllegalStateException(localizationUtils.getLocalizedMessage(MessageKeyExceptions.ROLE_DELETE_FAILED_USER_LOGIN_LINKED));
		} else {
			roleRepository.deleteById(roleId);
		}
	}

	@Override
	public Role getRoleByName(String name) throws Exception {
		Role role = roleRedisService.getRoleByName(name);
		if (role == null) {
			role = roleRepository.findByName(name);

			if (role == null) {
				throw new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeyExceptions.ROLE_NOT_FOUND));
			}

			roleRedisService.saveRoleByName(role.getName(), role);
		}

		return role;
	}

}
