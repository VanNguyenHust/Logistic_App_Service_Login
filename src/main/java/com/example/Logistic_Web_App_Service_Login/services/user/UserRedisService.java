package com.example.Logistic_Web_App_Service_Login.services.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.example.Logistic_Web_App_Service_Login.models.User;
import com.example.Logistic_Web_App_Service_Login.responses.UserResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserRedisService implements IUserRedisService {
	private final RedisTemplate<String, Object> redisTemplate;
	private final ObjectMapper redisObjectMapper;

	@Value("${spring.data.redis.use-redis-cache}")
	private boolean useRedisCache;

	@Override
	public void clearByUserId(Long userId) {
		redisTemplate.delete(getKeyFromUserId(userId));
	}

	private String getKeyFrom(String keyword, PageRequest pageRequest) {
		int pageNumber = pageRequest.getPageNumber();
		int pageSize = pageRequest.getPageSize();
		Sort sort = pageRequest.getSort();
		String sortDirection = sort.getOrderFor("id").getDirection() == Sort.Direction.ASC ? "asc" : "desc";
		String key = String.format("all_products:%s:%d:%d:%s", keyword, pageNumber, pageSize, sortDirection);
		return key;
	}

	@Override
	public List<UserResponse> getAllUserByKeyword(String keyword, PageRequest pageRequest) throws Exception {
		if (useRedisCache == false) {
			return null;
		}
		String key = this.getKeyFrom(keyword, pageRequest);
		String json = (String) redisTemplate.opsForValue().get(key);
		List<UserResponse> userResponses = json != null
				? redisObjectMapper.readValue(json, new TypeReference<List<UserResponse>>() {
				})
				: null;
		return userResponses;
	}

	@Override
	public void saveAllUserByKeyword(List<UserResponse> userResponses, String keyword, PageRequest pageRequest)
			throws Exception {
		String key = this.getKeyFrom(keyword, pageRequest);
		String json = redisObjectMapper.writeValueAsString(userResponses);
		redisTemplate.opsForValue().set(key, json);
	}

	private String getKeyFromUserId(Long userId) {
		String key = String.format("user_by_id:%d", userId);
		
		return key;
	}

	
	@Override
	public User getUserById(Long userId) throws Exception {
		if (useRedisCache == false) {
			return null;
		}
		String key = this.getKeyFromUserId(userId);
		String json = (String) redisTemplate.opsForValue().get(key);
		User user = json != null
				? redisObjectMapper.readValue(json, new TypeReference<User>() {
				})
				: null;
		return user;
	}

	@Override
	public void saveUserById(Long userId, User user) throws Exception{
		String key = this.getKeyFromUserId(userId);
		String json = redisObjectMapper.writeValueAsString(user);
		
		redisTemplate.opsForValue().set(key, json);
	}

}
