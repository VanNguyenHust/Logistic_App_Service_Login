package com.hust.globalict.main.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hust.globalict.main.modules.Token;
import com.hust.globalict.main.services.token.ITokenRedisService;

import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TokenListener {
	private final ITokenRedisService tokenRedisService;
	private static final Logger logger = LoggerFactory.getLogger(TokenListener.class);

	@PrePersist
	public void prePersist(Token token) {
		logger.info("prePersist");
	}

	@PostPersist // save = persis
	public void postPersist(Token token) {
		// Update Redis cache
		logger.info("postPersist");
		tokenRedisService.clear(token.getToken());
	}

	@PreUpdate
	public void preUpdate(Token token) {
		// ApplicationEventPublisher.instance().publishEvent(event);
		logger.info("preUpdate");
	}

	@PostUpdate
	public void postUpdate(Token token) {
		// Update Redis cache
		logger.info("postUpdate");
		tokenRedisService.clear(token.getToken());
	}

	@PreRemove
	public void preRemove(Token token) {
		// ApplicationEventPublisher.instance().publishEvent(event);
		logger.info("preRemove");
	}

	@PostRemove
	public void postRemove(Token token) {
		// Update Redis cache
		logger.info("postRemove");
		tokenRedisService.clear(token.getToken());
	}
}
