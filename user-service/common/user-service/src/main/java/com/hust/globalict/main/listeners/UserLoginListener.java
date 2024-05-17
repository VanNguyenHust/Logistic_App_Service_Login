package com.hust.globalict.main.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hust.globalict.main.modules.UserLogin;
import com.hust.globalict.main.services.userlogin.UserLoginRedisService;

import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UserLoginListener {
	private final UserLoginRedisService userLoginRedisService;
	private static final Logger logger = LoggerFactory.getLogger(UserLoginListener.class);

	@PrePersist
	public void prePersist(UserLogin userLogin) {
		logger.info("prePersist");
	}

	@PostPersist // save = persis
	public void postPersist(UserLogin userLogin) {
		// Update Redis cache
		logger.info("postPersist");
		userLoginRedisService.clear(userLogin);
	}

	@PreUpdate
	public void preUpdate(UserLogin userLogin) {
		// ApplicationEventPublisher.instance().publishEvent(event);
		logger.info("preUpdate");
	}

	@PostUpdate
	public void postUpdate(UserLogin userLogin) {
		// Update Redis cache
		logger.info("postUpdate");
		userLoginRedisService.clear(userLogin);
	}

	@PreRemove
	public void preRemove(UserLogin userLogin) {
		// ApplicationEventPublisher.instance().publishEvent(event);
		logger.info("preRemove");
	}

	@PostRemove
	public void postRemove(UserLogin userLogin) {
		// Update Redis cache
		logger.info("postRemove");
		userLoginRedisService.clear(userLogin);
	}
}
