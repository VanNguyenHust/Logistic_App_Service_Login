package com.example.Logistic_Web_App_Service_Login.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

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
public class ChangePasswordDTO {
	@JsonProperty("old_password")
	String oldPassword;
	
	@JsonProperty("new_password")
	String newPassword;
	
	@JsonProperty("re_new_password")
	String reNewPassword;
}	
