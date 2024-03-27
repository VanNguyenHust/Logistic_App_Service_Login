package com.example.Logistic_Web_App_Service_Login.modules.userlogin.response;

import com.example.Logistic_Web_App_Service_Login.common.entity.User;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserLoginResponse {
	@JsonProperty("message")
	String message;

	@JsonProperty("token")
	String token;

	@JsonProperty("refresh_token")
	String refreshToken;

	@JsonProperty("token_type")
	String tokenType = "Bearer";

	// user's detail
	Long id;

	@JsonProperty("user_name")
	String username;

	@JsonProperty("role_name")
	String roleName;
	
	@JsonProperty("user")
	User user;
}
