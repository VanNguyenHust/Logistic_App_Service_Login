package com.example.Logistic_Web_App_Service_Login.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.Logistic_Web_App_Service_Login.models.Tenant;
import com.example.Logistic_Web_App_Service_Login.services.tenant.TenantRedisService;

import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TenantListener {
	private final TenantRedisService tenantRedisService;
	private static final Logger logger = LoggerFactory.getLogger(TenantListener.class);

	@PrePersist
	public void prePersist(Tenant tenant) {
		logger.info("prePersist");
	}

	@PostPersist // save = persis
	public void postPersist(Tenant tenant) {
		// Update Redis cache
		logger.info("postPersist");
		tenantRedisService.clear(tenant.getId());
	}

	@PreUpdate
	public void preUpdate(Tenant tenant) {
		// ApplicationEventPublisher.instance().publishEvent(event);
		logger.info("preUpdate");
	}

	@PostUpdate
	public void postUpdate(Tenant tenant) {
		// Update Redis cache
		logger.info("postUpdate");
		tenantRedisService.clear(tenant.getId());
	}

	@PreRemove
	public void preRemove(Tenant tenant) {
		// ApplicationEventPublisher.instance().publishEvent(event);
		logger.info("preRemove");
	}

	@PostRemove
	public void postRemove(Tenant tenant) {
		// Update Redis cache
		logger.info("postRemove");
		tenantRedisService.clear(tenant.getId());
	}
}
