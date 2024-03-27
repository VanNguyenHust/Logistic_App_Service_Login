package com.example.Logistic_Web_App_Service_Login.common.components;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.Logistic_Web_App_Service_Login.common.entity.Token;
import com.example.Logistic_Web_App_Service_Login.common.entity.User;
import com.example.Logistic_Web_App_Service_Login.common.entity.UserLogin;
import com.example.Logistic_Web_App_Service_Login.common.repository.TokenRepository;

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
	@Value("${jwt.expiration}")
	private int expiration;
	@Value("${jwt.secretKey}")
	private String secretKey;
	
	private final TokenRepository tokenRepository;
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenUtil.class);

	public String generateToken(UserLogin userLogin) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("userName", userLogin.getUsername());

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
	
	public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }
	
	public boolean validateToken(String token, UserLogin userDetails, User user) {
        try {
            String userName = extractUserName(token);
            Token existingToken = tokenRepository.findByToken(token);
            if(existingToken == null ||
                    existingToken.isRevoked() == true ||
                    !(user.getIsActive() == 1)
            ) {
                return false;
            }
            return (userName.equals(userDetails.getUsername()))
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
