package com.hust.globalict.main.responses;

import java.time.LocalDateTime;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hust.globalict.main.modules.BaseEntity;
import com.hust.globalict.main.modules.Role;
import com.hust.globalict.main.modules.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonDeserialize(as = UserLoginResponse.class)
public class UserLoginResponse extends BaseEntity {
	// user's detail
	private Long id;

	@JsonProperty("user_name")
	private String username;

	@JsonProperty("login_type")
	private String loginType;

	@JsonProperty("role")
	private Role role;

	@JsonProperty("user")
	private User user;

	private Collection<? extends GrantedAuthority> authorities;

	private boolean accountNonExpired;

	private boolean accountNonLocked;

	private boolean credentialsNonExpired;

	private boolean enabled;

	@JsonProperty("created_at")
	private LocalDateTime createdAt;

	@JsonProperty("updated_at")
	private LocalDateTime updatedAt;
}
