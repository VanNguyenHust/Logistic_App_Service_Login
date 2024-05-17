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
public class TenantDTO {
	@NotBlank(message = MessageKeyValidation.TENANT_CODE_NOT_BLANK)
	private String code;

	@NotBlank(message = MessageKeyValidation.TENANT_NAME_NOT_BLANK)
	private String name;

	@JsonProperty("translate_name")
	private String translateName;

	private String status;

	@JsonProperty("user_id")
	private Long userId;
}
