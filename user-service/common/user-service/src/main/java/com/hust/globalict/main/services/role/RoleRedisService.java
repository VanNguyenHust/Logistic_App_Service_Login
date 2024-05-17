package com.hust.globalict.main.services.role;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hust.globalict.main.modules.Role;

import lombok.RequiredArgsConstructor;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class RoleRedisService implements IRoleRedisService {
	private final RedisTemplate<String, Object> redisTemplate;
	private final ObjectMapper redisObjectMapper;

	@Value("${spring.data.redis.use-redis-cache}")
	private boolean useRedisCache;

	@Override
	public void clear() {
		if (useRedisCache) {
			redisTemplate.delete(Objects.requireNonNull(redisTemplate.keys("role:*")));
		}
	}

	private String getKeyFromRoleId(Long roleId) {
		String key = String.format("role: id = %d", roleId);

		return key;
	}

	@Override
	public Role getRoleById(Long roleId) throws Exception {
		if (!useRedisCache) {
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

	private String getKeyFromName(String roleName) {
		String key = String.format("Role: name = %s", roleName);

		return key;
	}

	@Override
	public Role getRoleByName(String roleName) throws Exception {
		if (!useRedisCache) {
			return null;
		}

		String key = this.getKeyFromName(roleName);
		String json = (String) redisTemplate.opsForValue().get(key);

		return json != null
				? redisObjectMapper.readValue(json, new TypeReference<Role>() {
		})
				: null;
	}

	@Override
	public void saveRoleByName(String roleName, Role role) throws Exception {
		String key = this.getKeyFromName(roleName);
		String json = redisObjectMapper.writeValueAsString(role);

		redisTemplate.opsForValue().set(key, json);
	}
}
