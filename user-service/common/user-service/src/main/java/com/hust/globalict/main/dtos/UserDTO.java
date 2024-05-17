package com.hust.globalict.main.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hust.globalict.main.utils.MessageKeyValidation;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
	@JsonProperty("full_name")
	@NotBlank(message = MessageKeyValidation.USER_FULL_NAME_NOT_BLANK)
	private String fullName;

	@JsonProperty("first_name")
	@NotBlank(message = MessageKeyValidation.USER_FIRST_NAME_NOT_BLANK)
	private String firstName;

	@JsonProperty("last_name")
	@NotBlank(message = MessageKeyValidation.USER_LAST_NAME_NOT_BLANK)
	private String lastName;

	@JsonProperty("phone")
	private String phone;

	@JsonProperty("tenant_id")
	private Long tenantId;

	@JsonProperty("department_id")
	private Long departmentId;
}
