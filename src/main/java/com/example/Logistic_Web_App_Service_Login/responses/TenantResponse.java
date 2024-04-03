package com.example.Logistic_Web_App_Service_Login.responses;

import com.example.Logistic_Web_App_Service_Login.models.User;
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
public class TenantResponse {
	Long id;
	
	String code;
	
	String name;
	
	@JsonProperty("translate_name")
	String translateName;
	
	String status;
	
	User user;
}
