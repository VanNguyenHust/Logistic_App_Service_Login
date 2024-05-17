package com.hust.globalict.main.services.tenant;

import com.hust.globalict.main.modules.User;
import com.hust.globalict.main.services.user.IUserRedisService;
import com.hust.globalict.main.utils.MessageKeyExceptions;
import org.springframework.stereotype.Service;

import com.hust.globalict.main.components.LocalizationUtils;
import com.hust.globalict.main.exceptions.DataNotFoundException;
import com.hust.globalict.main.modules.Tenant;
import com.hust.globalict.main.repositories.TenantRepository;
import com.hust.globalict.main.repositories.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TenantService implements ITenantService {
	private final TenantRepository tenantRepository;
	private final ITenantRedisService tenantRedisService;

	private final UserRepository userRepository;
	private final IUserRedisService userRedisService;

	private final LocalizationUtils localizationUtils;

	@Override
	@Transactional
	public Tenant createTenant(Tenant tenant) throws Exception {
		if (tenantRepository.existsByNameOrCode(tenant.getName(), tenant.getCode())) {
			throw new RuntimeException(localizationUtils.getLocalizedMessage(MessageKeyExceptions.TENANT_NAME_OR_CODE_EXISTED));
		}

		tenantRepository.save(tenant);
		User existingUser = tenant.getUser();
		existingUser.setTenant(tenant);
		userRepository.save(existingUser);

		tenantRedisService.saveTenantById(tenant.getId(), tenant);
		userRedisService.saveUserById(existingUser.getId(), existingUser);

		return tenant;
	}

	@Override
	public Tenant getTenantById(Long tenantId) throws Exception {
		Tenant tenant = tenantRedisService.getTenantById(tenantId);
		if (tenant == null) {
			tenant = tenantRepository.findById(tenantId)
					.orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeyExceptions.TENANT_NOT_FOUND)));

			tenantRedisService.saveTenantById(tenantId, tenant);
		}

		return tenant;
	}

	@Override
	@Transactional
	public Tenant updateTenant(Long tenantId, Tenant tenant) throws Exception {
		tenant = tenantRepository.save(tenant);
		tenantRedisService.saveTenantById(tenantId, tenant);

		return tenant;
	}

	@Override
	public void deleteTenant(Long tenantId) throws Exception {
		Tenant existingTenant = getTenantById(tenantId);

		User user = userRepository.findByTenant(existingTenant);
		if (user != null) {
			user.setTenant(null);
			userRepository.save(user);
		}

		tenantRepository.delete(existingTenant);
	}

}
