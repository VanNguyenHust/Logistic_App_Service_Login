package com.hust.globalict.main.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentDTO {
	@JsonProperty("parent_department_id")
	private Long parentDepartmentId;

	@JsonProperty("parent_department_code")
	private String parentDepartmentCode;

	@JsonProperty("path")
	private String path;

	@JsonProperty("level")
	private int level;

	@JsonProperty("type")
	private String type;

	@JsonProperty("code")
	private String code;

	@JsonProperty("name")
	private String name;

	@JsonProperty("translate_name")
	private String translateName;

	@JsonProperty("status")
	private String status;
	
	@JsonProperty("user_id")
	private Long userId;
}
