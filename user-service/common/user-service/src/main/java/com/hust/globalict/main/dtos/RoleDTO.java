package com.hust.globalict.main.dtos;

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
public class RoleDTO {
	@NotBlank(message = MessageKeyValidation.ROLE_NAME_NOT_BLANK)
	private String name;
}
