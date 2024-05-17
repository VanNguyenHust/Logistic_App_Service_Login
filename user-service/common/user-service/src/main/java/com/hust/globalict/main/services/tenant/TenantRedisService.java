package com.hust.globalict.main.services.tenant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hust.globalict.main.modules.Tenant;

import lombok.RequiredArgsConstructor;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TenantRedisService implements ITenantRedisService{
	private final RedisTemplate<String, Object> redisTemplate;
	private final ObjectMapper redisObjectMapper;
	
	@Value("${spring.data.redis.use-redis-cache}")
	private boolean useRedisCache;
	
	@Override
	public void clear(Long tenantId) {
		if (useRedisCache) {
			redisTemplate.delete(Objects.requireNonNull(redisTemplate.keys(getKeyFromTenantId(tenantId))));
		}
	}
	
	private String getKeyFromTenantId(Long tenantId) {
		String key = String.format("tenant: id = %d", tenantId);

		return key;
	}
	
	@Override
	public Tenant getTenantById(Long tenantId) throws Exception {
		if (useRedisCache == false) {
			return null;
		}
		
		String key = this.getKeyFromTenantId(tenantId);
		String json = (String) redisTemplate.opsForValue().get(key);
		Tenant tenant = json != null ? redisObjectMapper.readValue(json, new TypeReference<Tenant>() {
		}) : null;
		
		return tenant;
	}

	@Override
	public void saveTenantById(Long tenantId, Tenant tenant) throws Exception {
		String key = this.getKeyFromTenantId(tenantId);
		String json = redisObjectMapper.writeValueAsString(tenant);

		redisTemplate.opsForValue().set(key, json);
	}

}
