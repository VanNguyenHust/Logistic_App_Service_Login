package com.hust.globalict.main.services.token;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hust.globalict.main.modules.Token;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenRedisService implements ITokenRedisService {
	private final RedisTemplate<String, Object> redisTemplate;
	private final ObjectMapper redisObjectMapper;

	@Value("${spring.data.redis.use-redis-cache}")
	private boolean useRedisCache;

	@Override
	public void clear(String token) {
		redisTemplate.delete(getKeyFromToken(token));
	}

	private String getKeyFromToken(String token) {
		String key = String.format("token: %s", token);

		return key;
	}

	@Override
	public Token getTokenEntityByToken(String token) throws Exception {
		if (!useRedisCache) {
			return null;
		}

		String key = this.getKeyFromToken(token);
		String json = (String) redisTemplate.opsForValue().get(key);
		Token tokenEntity = json != null ? redisObjectMapper.readValue(json, new TypeReference<Token>() {
		}) : null;

		return tokenEntity;
	}

	@Override
	public void saveTokenEntityByToken(String token, Token tokenEntity) throws Exception {
		String key = this.getKeyFromToken(token);
		String json = redisObjectMapper.writeValueAsString(tokenEntity);

		redisTemplate.opsForValue().set(key, json);
	}

}
