package com.hust.globalict.main.services.userlogin;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hust.globalict.main.modules.Token;
import com.hust.globalict.main.modules.UserLogin;

import lombok.RequiredArgsConstructor;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserLoginRedisService implements IUserLoginRedisService {
	private final RedisTemplate<String, Object> redisTemplate;
	private final ObjectMapper redisObjectMapper;

	@Value("${spring.data.redis.use-redis-cache}")
	private boolean useRedisCache;

	@SuppressWarnings("deprecation")
	@Override
	public void clear(UserLogin userLogin) {
		if (useRedisCache) {
			redisTemplate.delete(Objects.requireNonNull(redisTemplate.keys(String.format(getKeyFromUsername(userLogin.getUsername())))));
			redisTemplate.delete(Objects.requireNonNull(redisTemplate.keys(getKeyFrom(userLogin.getId()))));
			redisTemplate.delete(Objects.requireNonNull(redisTemplate.keys("user-login: token*")));
		}
	}

	private String getKeyFrom(Long userLoginId) {
		String key = String.format("user_login: id = %d", userLoginId);

		return key;
	}

	@Override
	public UserLogin getUserLoginById(Long userLoginId) throws Exception {
		if (!useRedisCache) {
			return null;
		}

		String key = this.getKeyFrom(userLoginId);
		String json = (String) redisTemplate.opsForValue().get(key);

		UserLogin userLogin = json != null
				? redisObjectMapper.readValue(json, new TypeReference<UserLogin>() {
				})
				: null;

		return userLogin;
	}

	@Override
	public void saveUserLoginById(Long userLoginId, UserLogin userLogin) throws Exception {
		String key = this.getKeyFrom(userLoginId);
		String json = redisObjectMapper.writeValueAsString(userLogin);

		redisTemplate.opsForValue().set(key, json);
	}

	private String getKeyFromTokenOrRefreshToken(String tokenOrRefreshToken, String tokenType) {
		String key = String.format("user-login: token - refresh_token = %s, token_type = %s", tokenOrRefreshToken, tokenType);

		return key;
	}

	@Override
	public UserLogin getUserLoginDetailsFromTokenOrRefreshToken(String tokenOrRefreshToken, String tokenType) throws Exception {
		if (!useRedisCache) {
			return null;
		}

		String key = this.getKeyFromTokenOrRefreshToken(tokenOrRefreshToken, tokenType);
		String json = (String) redisTemplate.opsForValue().get(key);

		UserLogin userLogin = json != null ? redisObjectMapper.readValue(json, new TypeReference<UserLogin>() {
		}) : null;

		return userLogin;
	}

	@Override
	public void saveUserLoginByTokenOrRefreshToken(String tokenOrRefreshToken, String tokenType, UserLogin userLogin) throws Exception {
		String keyRefreshToken = this.getKeyFromTokenOrRefreshToken(tokenOrRefreshToken, tokenType);
		String jsonRefreshToken = redisObjectMapper.writeValueAsString(userLogin);
		redisTemplate.opsForValue().set(keyRefreshToken, jsonRefreshToken);
	}

	private String getKeyFromUsername(String username) {
		String key = String.format("User-login: username = %s", username);

		return key;
	}

	@Override
	public UserLogin loadUserByUsername(String username) throws Exception {
		if (!useRedisCache) {
			return null;
		}

		String key = this.getKeyFromUsername(username);
		String json = (String) redisTemplate.opsForValue().get(key);

		UserLogin userLogin = json != null ? redisObjectMapper.readValue(json, new TypeReference<UserLogin>() {
		}) : null;

		return userLogin;
	}

	@Override
	public void saveUserLoginByUsername(String username, UserLogin userLogin) throws Exception {
		String key = this.getKeyFromUsername(username);
		String json = redisObjectMapper.writeValueAsString(userLogin);

		redisTemplate.opsForValue().set(key, json);
	}
}
