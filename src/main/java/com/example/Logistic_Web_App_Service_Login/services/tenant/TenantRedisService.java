package com.example.Logistic_Web_App_Service_Login.services.tenant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.example.Logistic_Web_App_Service_Login.responses.TenantResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TenantRedisService implements ITenantRedisService{
	private final RedisTemplate<String, Object> redisTemplate;
	private final ObjectMapper redisObjectMapper;
	
	@Value("${spring.data.redis.use-redis-cache}")
	private boolean useRedisCache;
	
	@SuppressWarnings("deprecation")
	@Override
	public void clear() {
		redisTemplate.getConnectionFactory().getConnection().flushAll();
	}
	
	private String getKeyFromTenantId(Long tenantId) {
		String key = String.format("tenant_by_id:%d", tenantId);

		return key;
	}
	
	@Override
	public TenantResponse getTenantById(Long tenantId) throws Exception {
		if (useRedisCache == false) {
			return null;
		}
		
		String key = this.getKeyFromTenantId(tenantId);
		String json = (String) redisTemplate.opsForValue().get(key);
		TenantResponse tenantResponse = json != null ? redisObjectMapper.readValue(json, new TypeReference<TenantResponse>() {
		}) : null;
		
		return tenantResponse;
	}

	@Override
	public void saveTenantById(Long tenantId, TenantResponse tenantResponse) throws Exception {
		String key = this.getKeyFromTenantId(tenantId);
		String json = redisObjectMapper.writeValueAsString(tenantResponse);

		redisTemplate.opsForValue().set(key, json);
	}

}
