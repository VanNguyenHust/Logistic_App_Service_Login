package com.hust.globalict.main.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.hust.globalict.main.validations.userlogin.ValidPassword;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordDTO {
	@JsonProperty("old_password")
	@ValidPassword
	private String oldPassword;

	@JsonProperty("new_password")
	private String newPassword;

	@JsonProperty("re_new_password")
	private String reNewPassword;
}
