package com.example.Logistic_Web_App_Service_Login.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Data // toString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class RefreshTokenDTO {
	@NotBlank
	@JsonProperty("refresh_token")
	String refreshToken;
	
	@NotBlank
	@JsonProperty("token_type")
	String tokenType = "Bearer";
}
