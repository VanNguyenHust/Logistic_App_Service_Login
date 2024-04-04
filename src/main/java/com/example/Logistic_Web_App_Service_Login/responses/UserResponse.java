package com.example.Logistic_Web_App_Service_Login.responses;

import java.time.LocalDateTime;

import com.example.Logistic_Web_App_Service_Login.models.Tenant;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
	Long id;

	@JsonProperty("full_name")
	String fullName;

	@JsonProperty("first_name")
	String firstName;

	@JsonProperty("last_name")
	String lastName;

	@JsonProperty("is_active")
	short isActive;

	Tenant tenant;
	
	@JsonProperty("created_at")
	LocalDateTime createdAt;

	@JsonProperty("updated_at")
	LocalDateTime updatedAt;
}
