package com.example.Logistic_Web_App_Service_Login.services.userlogin;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import com.example.Logistic_Web_App_Service_Login.models.Role;
import com.example.Logistic_Web_App_Service_Login.models.Token;
import com.example.Logistic_Web_App_Service_Login.models.User;
import com.example.Logistic_Web_App_Service_Login.models.UserLogin;
import com.example.Logistic_Web_App_Service_Login.responses.UserLoginResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserLoginRedisService implements IUserLoginRedisService {
	private final RedisTemplate<String, Object> redisTemplate;
	private final ObjectMapper redisObjectMapper;

	@Value("${spring.data.redis.use-redis-cache}")
	private boolean useRedisCache;

	@SuppressWarnings("deprecation")
	@Override
	public void clear() {
		redisTemplate.getConnectionFactory().getConnection().flushAll();
	}

	private String getKeyFrom(Long userLoginId) {
		String key = String.format("user_login_by_id:%d", userLoginId);

		return key;
	}

	@Override
	public UserLoginResponse getUserLogin(Long userLoginId) throws Exception {
		if (useRedisCache == false) {
			return null;
		}

		String key = this.getKeyFrom(userLoginId);
		String json = (String) redisTemplate.opsForValue().get(key);

		UserLoginResponse userLoginResponse = json != null
				? redisObjectMapper.readValue(json, new TypeReference<UserLoginResponse>() {
				})
				: null;

		return userLoginResponse;
	}

	@Override
	public void saveUserLogin(Long userLoginId, UserLoginResponse userLoginResponse) throws Exception {
		String key = this.getKeyFrom(userLoginId);
		String json = redisObjectMapper.writeValueAsString(userLoginResponse);

		redisTemplate.opsForValue().set(key, json);
	}

	private String getKeyFromToken(String token, String tokenType) {
		String key = String.format("token:%s:%s", token, tokenType);

		return key;
	}

	private String getKeyFromRefreshToken(String refreshToken, String tokenType) {
		String key = String.format("refresh-token:%s:%s", refreshToken, tokenType);

		return key;
	}

	@Override
	public UserLogin getUserLoginDetailsFromToken(String token, String tokenType) throws Exception {
		if (useRedisCache == false) {
			return null;
		}

		String key = this.getKeyFromToken(token, tokenType);
		String json = (String) redisTemplate.opsForValue().get(key);

		UserLogin userLogin = json != null ? redisObjectMapper.readValue(json, new TypeReference<UserLogin>() {
		}) : null;

		return userLogin;
	}

	@Override
	public UserLogin getUserLoginDetailsFromRefreshToken(String refreshToken, String tokenType) throws Exception {
		if (useRedisCache == false) {
			return null;
		}

		String key = this.getKeyFromToken(refreshToken, tokenType);
		String json = (String) redisTemplate.opsForValue().get(key);

		UserLogin userLogin = json != null ? redisObjectMapper.readValue(json, new TypeReference<UserLogin>() {
		}) : null;

		return userLogin;
	}

	@Override
	public void saveToken(Token token, UserLogin userLogin) throws Exception {
		String keyToken = this.getKeyFromToken(token.getToken(), token.getTokenType());
		String jsonToken = redisObjectMapper.writeValueAsString(userLogin);

		redisTemplate.opsForValue().set(keyToken, jsonToken);

		String keyRefreshToken = this.getKeyFromRefreshToken(token.getRefreshToken(), token.getTokenType());
		String jsonRefreshToken = redisObjectMapper.writeValueAsString(userLogin);

		redisTemplate.opsForValue().set(keyRefreshToken, jsonRefreshToken);
	}

	private String getKeyFromUsername(String username) {
		String key = String.format("User-login:%s", username);

		return key;
	}

	private UserLoginResponse convertJsonToUserLoginResponse(String json) throws Exception {
		JSONObject jsonObject = new JSONObject(json);

		Long id = jsonObject.getLong("id");
		String username = jsonObject.getString("user_name");
		String loginType = jsonObject.getString("login_type");

		JSONObject roleObject = jsonObject.getJSONObject("role");
		Role role = new Role();
		role.setId(roleObject.getLong("id"));
		role.setName(roleObject.getString("name"));

		boolean accountNonExpired = jsonObject.getBoolean("accountNonExpired");
		boolean accountNonLocked = jsonObject.getBoolean("accountNonLocked");
		boolean credentialsNonExpired = jsonObject.getBoolean("credentialsNonExpired");
		boolean enabled = jsonObject.getBoolean("enabled");

		JSONObject userObject = jsonObject.getJSONObject("user");
		User user = new User();
		user.setCreatedAt(
				LocalDateTime.parse(userObject.getString("createdAt"), DateTimeFormatter.ISO_LOCAL_DATE_TIME));
		user.setUpdatedAt(
				LocalDateTime.parse(userObject.getString("updatedAt"), DateTimeFormatter.ISO_LOCAL_DATE_TIME));
		user.setId(jsonObject.getLong("id"));
		user.setFullName(userObject.getString("fullName"));
		user.setIsActive((short) userObject.getInt("isActive"));

		List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
		authorityList.add(new SimpleGrantedAuthority(
				jsonObject.getJSONArray("authorities").getJSONObject(0).getString("authority")));

		UserLoginResponse userLoginResponse = new UserLoginResponse(id, username, loginType, role, user, authorityList,
				accountNonExpired, accountNonLocked, credentialsNonExpired, enabled);

		return userLoginResponse;
	}

	@Override
	public UserLoginResponse loadUserByUsername(String username) throws Exception {
		if (useRedisCache == false) {
			return null;
		}

		String key = this.getKeyFromUsername(username);
		String json = (String) redisTemplate.opsForValue().get(key);

		UserLoginResponse userLoginResponse = json != null ? convertJsonToUserLoginResponse(json) : null;

		return userLoginResponse;
	}

	@Override
	public void saveUserLoginByUsername(String username, UserLoginResponse userLoginResponse) throws Exception {
		String key = this.getKeyFromUsername(username);
		String json = redisObjectMapper.writeValueAsString(userLoginResponse);

		redisTemplate.opsForValue().set(key, json);
	}
}
