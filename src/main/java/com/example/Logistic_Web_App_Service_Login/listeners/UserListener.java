package com.example.Logistic_Web_App_Service_Login.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.Logistic_Web_App_Service_Login.models.User;
import com.example.Logistic_Web_App_Service_Login.services.user.UserRedisService;

import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UserListener {
	private final UserRedisService userRedisService;
	private static final Logger logger = LoggerFactory.getLogger(UserListener.class);

	@PrePersist
	public void prePersist(User user) {
		logger.info("prePersist");
	}

	@PostPersist // save = persis
	public void postPersist(User user) {
		// Update Redis cache
		logger.info("postPersist");
		userRedisService.clearByUserId(user.getId());
	}

	@PreUpdate
	public void preUpdate(User user) {
		// ApplicationEventPublisher.instance().publishEvent(event);
		logger.info("preUpdate");
	}

	@PostUpdate
	public void postUpdate(User user) {
		// Update Redis cache
		logger.info("postUpdate");
		userRedisService.clearByUserId(user.getId());
	}

	@PreRemove
	public void preRemove(User user) {
		// ApplicationEventPublisher.instance().publishEvent(event);
		logger.info("preRemove");
	}

	@PostRemove
	public void postRemove(User user) {
		// Update Redis cache
		logger.info("postRemove");
		userRedisService.clearByUserId(user.getId());
	}
}
