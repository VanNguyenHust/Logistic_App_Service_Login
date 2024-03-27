package com.example.Logistic_Web_App_Service_Login.modules.token.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.Logistic_Web_App_Service_Login.common.components.JwtTokenUtil;
import com.example.Logistic_Web_App_Service_Login.common.entity.Token;
import com.example.Logistic_Web_App_Service_Login.common.entity.UserLogin;
import com.example.Logistic_Web_App_Service_Login.common.exceptions.DataNotFoundException;
import com.example.Logistic_Web_App_Service_Login.common.exceptions.ExpiredTokenException;
import com.example.Logistic_Web_App_Service_Login.common.repository.TokenRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenService implements ITokenService {
	private static final int MAX_TOKENS = 3;
	@Value("${jwt.expiration}")
	private int expiration; // save to an environment variable

	@Value("${jwt.expiration-refresh-token}")
	private int expirationRefreshToken;

	private final TokenRepository tokenRepository;
	private final JwtTokenUtil jwtTokenUtil;

	@Transactional
	@Override
	public Token refreshToken(String refreshToken, UserLogin userLogin) throws Exception {
		Token existingToken = tokenRepository.findByRefreshToken(refreshToken);
		if (existingToken == null) {
			throw new DataNotFoundException("Refresh token does not exist");
		}
		if (existingToken.getRefreshExpirationDate().compareTo(LocalDateTime.now()) < 0) {
			tokenRepository.delete(existingToken);
			throw new ExpiredTokenException("Refresh token is expired");
		}
		String token = jwtTokenUtil.generateToken(userLogin);
		LocalDateTime expirationDateTime = LocalDateTime.now().plusSeconds(expiration);
		existingToken.setExpirationDate(expirationDateTime);
		existingToken.setToken(token);
		existingToken.setRefreshToken(UUID.randomUUID().toString());
		existingToken.setRefreshExpirationDate(LocalDateTime.now().plusSeconds(expirationRefreshToken));
		return existingToken;
	}

	@Transactional
	@Override
	public Token addToken(UserLogin userLogin, String token, boolean isMobileDevice) {
		List<Token> userLoginTokens = tokenRepository.findByUserLogin(userLogin);
		int tokenCount = userLoginTokens.size();
		// Số lượng token vượt quá giới hạn, xóa một token cũ
		if (tokenCount >= MAX_TOKENS) {
			// kiểm tra xem trong danh sách userTokens có tồn tại ít nhất
			// một token không phải là thiết bị di động (non-mobile)
			boolean hasNonMobileToken = !userLoginTokens.stream().allMatch(Token::isMobile);
			Token tokenToDelete;
			if (hasNonMobileToken) {
				tokenToDelete = userLoginTokens.stream().filter(userToken -> !userToken.isMobile()).findFirst()
						.orElse(userLoginTokens.get(0));
			} else {
				// tất cả các token đều là thiết bị di động,
				// chúng ta sẽ xóa token đầu tiên trong danh sách
				tokenToDelete = userLoginTokens.get(0);
			}
			tokenRepository.delete(tokenToDelete);
		}
		long expirationInSeconds = expiration;
		LocalDateTime expirationDateTime = LocalDateTime.now().plusSeconds(expirationInSeconds);
		// Tạo mới một token cho người dùng
		Token newToken = Token.builder()
				.userLogin(userLogin)
				.token(token)
				.revoked(false)
				.expired(false)
				.tokenType("Bearer")
				.expirationDate(expirationDateTime)
				.isMobile(isMobileDevice)
				.build();

		newToken.setRefreshToken(UUID.randomUUID().toString());
		newToken.setRefreshExpirationDate(LocalDateTime.now().plusSeconds(expirationRefreshToken));
		tokenRepository.save(newToken);
		return newToken;
	}
}
