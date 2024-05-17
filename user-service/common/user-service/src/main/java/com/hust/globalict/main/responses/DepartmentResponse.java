package com.hust.globalict.main.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hust.globalict.main.modules.Tenant;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DepartmentResponse {
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

    @JsonProperty("created_at")
    LocalDateTime createdAt;

    @JsonProperty("updated_at")
    LocalDateTime updatedAt;
}
