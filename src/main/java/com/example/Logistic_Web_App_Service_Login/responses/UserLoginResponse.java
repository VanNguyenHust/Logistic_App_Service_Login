package com.example.Logistic_Web_App_Service_Login.responses;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

import com.example.Logistic_Web_App_Service_Login.models.BaseEntity;
import com.example.Logistic_Web_App_Service_Login.models.Role;
import com.example.Logistic_Web_App_Service_Login.models.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

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
@JsonDeserialize(as = UserLoginResponse.class)
public class UserLoginResponse extends BaseEntity{
	// user's detail
	Long id;

	@JsonProperty("user_name")
	String username;
	
	@JsonProperty("login_type")
	String loginType;
	
	@JsonProperty("role")
	Role role;
	
	@JsonProperty("user")
	User user;
	
	Collection<? extends GrantedAuthority> authorities;
	
	boolean accountNonExpired;
	
	boolean accountNonLocked;
	
	boolean credentialsNonExpired;
	
	boolean enabled;
}
