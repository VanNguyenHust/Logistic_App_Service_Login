package com.hust.globalict.main.services.department;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hust.globalict.main.modules.Department;

import lombok.RequiredArgsConstructor;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class DepartmentRedisService implements IDepartmentRedisService {
	private final RedisTemplate<String, Object> redisTemplate;
	private final ObjectMapper redisObjectMapper;

	@Value("${spring.data.redis.use-redis-cache}")
	private boolean useRedisCache;

	@Override
	public void clear(Long departmentId) {
		if (useRedisCache) {
			redisTemplate.delete(Objects.requireNonNull(redisTemplate.keys(getKeyFromDepartmentId(departmentId))));
		}
	}

	private String getKeyFromDepartmentId(Long id) {
		String key = String.format("department: id = %d", id);

		return key;
	}

	@Override
	public Department getDepartmentById(Long departmentId) throws Exception {
		if (!useRedisCache) {
			return null;
		}
		
		String key = this.getKeyFromDepartmentId(departmentId);
		String json = (String) redisTemplate.opsForValue().get(key);
		Department department = json != null ? redisObjectMapper.readValue(json, new TypeReference<Department>() {
		}) : null;
		
		return department;
	}

	@Override
	public void saveDepartmentById(Long departmentId, Department department) throws Exception {
		String key = this.getKeyFromDepartmentId(departmentId);
		String json = redisObjectMapper.writeValueAsString(department);

		redisTemplate.opsForValue().set(key, json);
	}

}
