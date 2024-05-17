package com.hust.globalict.main.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data // toString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefreshTokenDTO {
	@NotBlank
	@JsonProperty("refresh_token")
	private String refreshToken;

	@NotBlank
	@JsonProperty("token_type")
	private String tokenType;
}
