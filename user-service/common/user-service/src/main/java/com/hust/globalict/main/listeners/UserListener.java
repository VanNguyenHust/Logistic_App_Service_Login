package com.hust.globalict.main.listeners;

import com.hust.globalict.main.services.user.IUserRedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hust.globalict.main.modules.User;
import com.hust.globalict.main.services.user.UserRedisService;

import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UserListener {
	private final IUserRedisService userRedisService;
	private static final Logger logger = LoggerFactory.getLogger(UserListener.class);

	@PrePersist
	public void prePersist(User user) {
		logger.info("prePersist");
	}

	@PostPersist // save = persis
	public void postPersist(User user) {
		// Update Redis cache
		logger.info("postPersist");
		userRedisService.clearUser(user);
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
		userRedisService.clearUser(user);
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
		userRedisService.clearUser(user);
	}
}
