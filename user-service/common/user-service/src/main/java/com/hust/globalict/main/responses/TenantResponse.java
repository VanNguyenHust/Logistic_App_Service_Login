package com.hust.globalict.main.responses;

import java.time.LocalDateTime;

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
		
	@JsonProperty("created_at")
	LocalDateTime createdAt;

	@JsonProperty("updated_at")
	LocalDateTime updatedAt;
}
