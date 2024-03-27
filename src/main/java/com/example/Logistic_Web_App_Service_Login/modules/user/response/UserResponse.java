package com.example.Logistic_Web_App_Service_Login.modules.user.response;

import java.util.List;

import com.example.Logistic_Web_App_Service_Login.common.entity.User;
import com.fasterxml.jackson.annotation.JsonProperty;

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
public class UserResponse {
	@JsonProperty("message")
	private String message;

	@JsonProperty("errors")
	private List<String> errors;

	@JsonProperty("category")
	private User user;
}
