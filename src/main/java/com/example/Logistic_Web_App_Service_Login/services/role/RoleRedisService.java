package com.example.Logistic_Web_App_Service_Login.services.role;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.example.Logistic_Web_App_Service_Login.models.Role;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleRedisService implements IRoleRedisService {
	private final RedisTemplate<String, Object> redisTemplate;
	private final ObjectMapper redisObjectMapper;

	@Value("${spring.data.redis.use-redis-cache}")
	private boolean useRedisCache;

	@SuppressWarnings("deprecation")
	@Override
	public void clear() {
		redisTemplate.getConnectionFactory().getConnection().flushAll();

	}

	private String getKeyFromRoleId(Long roleId) {
		String key = String.format("role_by_id:%d", roleId);

		return key;
	}

	@Override
	public Role getRoleById(Long roleId) throws Exception {
		if (useRedisCache == false) {
			return null;
		}
		
		String key = this.getKeyFromRoleId(roleId);
		String json = (String) redisTemplate.opsForValue().get(key);
		Role role = json != null ? redisObjectMapper.readValue(json, new TypeReference<Role>() {
		}) : null;
		
		return role;
	}

	@Override
	public void saveRoleById(Long roleId, Role role) throws Exception {
		String key = this.getKeyFromRoleId(roleId);
		String json = redisObjectMapper.writeValueAsString(role);

		redisTemplate.opsForValue().set(key, json);
	}

}
