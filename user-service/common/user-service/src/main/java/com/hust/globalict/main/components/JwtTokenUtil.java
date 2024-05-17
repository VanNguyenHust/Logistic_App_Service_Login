package com.hust.globalict.main.components;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import com.hust.globalict.main.services.token.ITokenRedisService;
import com.hust.globalict.main.utils.MessageKeyExceptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hust.globalict.main.modules.Token;
import com.hust.globalict.main.modules.User;
import com.hust.globalict.main.modules.UserLogin;
import com.hust.globalict.main.repositories.TokenRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtTokenUtil {
	private final TokenRepository tokenRepository;
	private final ITokenRedisService tokenRedisService;
	private final ObjectMapper objectMapper;
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenUtil.class);
	private final LocalizationUtils localizationUtils;

	@Value("${jwt.expiration}")
	private int expiration;
	@Value("${jwt.secretKey}")
	private String secretKey;

	public String generateToken(UserLogin userLogin) throws JsonProcessingException {
		Map<String, Object> claims = new HashMap<>();
		claims.put("userName", userLogin.getUsername());
		
		String userLoginJson = objectMapper.writeValueAsString(userLogin);
		claims.put("userLogin", userLoginJson);

		try {
			String token = Jwts.builder()
					.setClaims(claims)
					.setSubject(userLogin.getUsername())
					.setExpiration(new Date(System.currentTimeMillis() + expiration * 1000L))
					.signWith(getSignKey(), SignatureAlgorithm.HS256)
					.compact();
			
			return token;
		} catch (Exception e) {
			System.err.println("Cannot create jwt token, erorr " + e.getMessage());
			return null;
		}
	}

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
        return extractClaim(token, claims -> (String) claims.get("userName"));
    }
	
	public boolean validateToken(String token, UserLogin userDetails, User user) throws Exception {
        try {	
            String username = extractUsername(token);
            Token existingToken = tokenRedisService.getTokenEntityByToken(token);
            if (existingToken == null) {
            	existingToken = tokenRepository.findByToken(token);

            	tokenRedisService.saveTokenEntityByToken(token, existingToken);
            }
            if(existingToken == null ||
                    existingToken.isRevoked() ||
                    !(user.getIsActive() == 1)
            ) {
                return false;
            }
            return (username.equals(userDetails.getUsername()))
                    && !isTokenExpired(token);
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }
}
