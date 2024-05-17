package com.hust.globalict.main.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hust.globalict.main.modules.Department;
import com.hust.globalict.main.services.department.IDepartmentRedisService;

import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DepartmentListener {
	private final IDepartmentRedisService departmentRedisService;
	private static final Logger logger = LoggerFactory.getLogger(DepartmentListener.class);

	@PrePersist
	public void prePersist(Department department) {
		logger.info("prePersist");
	}

	@PostPersist // save = persis
	public void postPersist(Department department) {
		// Update Redis cache
		logger.info("postPersist");
		departmentRedisService.clear(department.getId());
	}

	@PreUpdate
	public void preUpdate(Department department) {
		// ApplicationEventPublisher.instance().publishEvent(event);
		logger.info("preUpdate");
	}

	@PostUpdate
	public void postUpdate(Department department) {
		// Update Redis cache
		logger.info("postUpdate");
		departmentRedisService.clear(department.getId());
	}

	@PreRemove
	public void preRemove(Department department) {
		// ApplicationEventPublisher.instance().publishEvent(event);
		logger.info("preRemove");
	}

	@PostRemove
	public void postRemove(Department department) {
		// Update Redis cache
		logger.info("postRemove");
		departmentRedisService.clear(department.getId());
	}
}
