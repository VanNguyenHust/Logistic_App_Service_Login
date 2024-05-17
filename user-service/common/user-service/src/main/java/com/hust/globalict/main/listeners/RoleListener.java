package com.hust.globalict.main.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hust.globalict.main.modules.Role;
import com.hust.globalict.main.services.role.IRoleRedisService;

import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RoleListener {
	private final IRoleRedisService roleRedisService;
	private static final Logger logger = LoggerFactory.getLogger(RoleListener.class);

	@PrePersist
	public void prePersist(Role tenant) {
		logger.info("prePersist");
	}

	@PostPersist // save = persis
	public void postPersist(Role role) {
		// Update Redis cache
		logger.info("postPersist");
		roleRedisService.clear();
	}

	@PreUpdate
	public void preUpdate(Role role) {
		// ApplicationEventPublisher.instance().publishEvent(event);
		logger.info("preUpdate");
	}

	@PostUpdate
	public void postUpdate(Role role) {
		// Update Redis cache
		logger.info("postUpdate");
		roleRedisService.clear();
	}

	@PreRemove
	public void preRemove(Role role) {
		// ApplicationEventPublisher.instance().publishEvent(event);
		logger.info("preRemove");
	}

	@PostRemove
	public void postRemove(Role role) {
		// Update Redis cache
		logger.info("postRemove");
		roleRedisService.clear();
	}
}
