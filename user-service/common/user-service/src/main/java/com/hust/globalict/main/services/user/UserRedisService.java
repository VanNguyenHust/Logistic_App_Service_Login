package com.hust.globalict.main.services.user;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hust.globalict.main.modules.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserRedisService implements IUserRedisService {
	private final RedisTemplate<String, Object> redisTemplate;
	private final ObjectMapper redisObjectMapper;

	@Value("${spring.data.redis.use-redis-cache}")
	private boolean useRedisCache;

	@Override
	public void clearUser(User user) {
		if (useRedisCache) {
			redisTemplate.delete(Objects.requireNonNull(redisTemplate.keys("user: all*")));
			redisTemplate.delete(Objects.requireNonNull(getKeyFromUserId(user.getId())));
		}
	}

	private String getKeyFrom(String keyword, PageRequest pageRequest) {
		int pageNumber = pageRequest.getPageNumber();
		int pageSize = pageRequest.getPageSize();
		Sort sort = pageRequest.getSort();
		String sortDirection = Objects.requireNonNull(sort.getOrderFor("id")).getDirection() == Sort.Direction.ASC ? "asc" : "desc";
		String key = String.format("user: all - keyword = %s, pagenumber = %d, pageSize = %d, sort = %s", keyword, pageNumber, pageSize, sortDirection);
		return key;
	}

	@Override
	public List<User> getAllUserByKeyword(String keyword, PageRequest pageRequest) throws Exception {
		if (!useRedisCache) {
			return null;
		}
		String key = this.getKeyFrom(keyword, pageRequest);
		String json = (String) redisTemplate.opsForValue().get(key);
		List<User> users = json != null
				? redisObjectMapper.readValue(json, new TypeReference<List<User>>() {
				})
				: null;
		return users;
	}

	@Override
	public void saveAllUserByKeyword(List<User> users, String keyword, PageRequest pageRequest)
			throws Exception {
		String key = this.getKeyFrom(keyword, pageRequest);
		String json = redisObjectMapper.writeValueAsString(users);
		redisTemplate.opsForValue().set(key, json);
	}

	private String getKeyFromUserId(Long userId) {
		String key = String.format("user: id = %d", userId);
		
		return key;
	}

	
	@Override
	public User getUserById(Long userId) throws Exception {
		if (!useRedisCache) {
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
