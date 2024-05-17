package com.hust.globalict.main.services.token;

import com.hust.globalict.main.modules.Token;

public interface ITokenRedisService {
	void clear(String token);
	
	Token getTokenEntityByToken(String token) throws Exception;
	
	void saveTokenEntityByToken(String token, Token tokenEntity) throws Exception;
}
