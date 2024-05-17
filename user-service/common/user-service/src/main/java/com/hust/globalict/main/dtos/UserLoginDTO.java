package com.hust.globalict.main.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hust.globalict.main.utils.MessageKeyValidation;

import com.hust.globalict.main.validations.userlogin.ValidPassword;
import com.hust.globalict.main.validations.userlogin.ValidUsername;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginDTO {
	@ValidUsername
	@NotBlank(message = MessageKeyValidation.USER_LOGIN_USERNAME_NOT_BLANK)
	private String username;

	@ValidPassword
	@NotBlank(message = MessageKeyValidation.USER_LOGIN_PASSWORD_NOT_BLANK)
	private String password;

	@JsonProperty("retype_password")
	private String retypePassword;

	@NotBlank(message = MessageKeyValidation.USER_LOGIN_LOGIN_TYPE_NOT_BLANK)
	@JsonProperty("login_type")
	private String loginType;

	@JsonProperty("user_id")
	private Long userId;

	@JsonProperty("role_id")
	private Long roleId;
}
