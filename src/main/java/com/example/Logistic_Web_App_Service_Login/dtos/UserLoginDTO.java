package com.example.Logistic_Web_App_Service_Login.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserLoginDTO {
	@NotBlank(message = "Phone number is required")
	@JsonProperty("user_name")
	String userName;

	@NotBlank(message = "Password cannot be blank")
	String password;
	
	@JsonProperty("retype_password")
    private String retypePassword;
	
	@NotBlank(message = "Login type is required")
	@JsonProperty("login_type")
	String loginType;
	
	@JsonProperty("user_id")
	Long userId;
	
	@JsonProperty("role_id")
	Long roleId;
}
