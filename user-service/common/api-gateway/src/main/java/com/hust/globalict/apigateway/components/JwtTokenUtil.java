package com.hust.globalict.apigateway.components;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtTokenUtil {	
	@Value("${jwt.expiration}")
	private int expiration;
	@Value("${jwt.secretKey}")
	private String secretKey;

	private Key getSignKey() {
		byte[] bytes = Decoders.BASE64.decode(secretKey);
		return Keys.hmacShaKeyFor(bytes);
	}
	
	private Claims extractAllClaims(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(getSignKey())
				.build()
				.parseClaimsJws(token)
				.getBody();
	}
	
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = this.extractAllClaims(token);
		return claimsResolver.apply(claims);
	}
	
	public boolean isTokenExpired(String token) {
		Date expirationDate = this.extractClaim(token, Claims::getExpiration);
		
		return expirationDate.before(new Date());
	}
	
	public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
	
	public Map<String, String> extractUser(String token) {
		String userLoginJson = extractClaim(token, claims -> (String) claims.get("userLogin"));
		Map<String, String> data = new HashMap<>();
		
		JSONObject jsonObject = new JSONObject(userLoginJson);
		data.put(HeaderName.USERNAME, jsonObject.getString("username"));

		Long userId = 0L;
		String phone = null;
		Long tenantId = 0L;
		Long departmentId = 0L;
		String departmentCode = null;
		if (!jsonObject.isNull("user")) {
			JSONObject userObject = jsonObject.getJSONObject("user");
			userId = userObject.getLong("id");
			if (!userObject.isNull("phone")) {
				phone = userObject.getString("phone");
			}

			if (!userObject.isNull("tenant")) {
				JSONObject tenantObject = userObject.getJSONObject("tenant");
				tenantId = tenantObject.getLong("id");
			}

			if (!userObject.isNull("department")) {
				JSONObject departmentObject = userObject.getJSONObject("department");
				departmentId = departmentObject.getLong("id");
				if (!userObject.isNull("departmentCode")) {
					departmentCode = departmentObject.getString("code");
				}
			}
		}

        data.put(HeaderName.USER_ID, String.valueOf(userId));
		data.put(HeaderName.PHONE, phone);
		data.put(HeaderName.TENANT_ID, String.valueOf(tenantId));
		data.put(HeaderName.POST_ID, String.valueOf(departmentId));
		data.put(HeaderName.POST_CODE, departmentCode);
        
	    return data;
	}
}
