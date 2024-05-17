package com.hust.globalict.main.modules;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hust.globalict.main.listeners.DepartmentListener;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "departments")
@EntityListeners(DepartmentListener.class)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Department extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "parent_department_id")
	private Long parentDepartmentId;
	
	@Column(name = "parent_department_code")
	private String parentDepartmentCode;

	@Column(name = "path")
	private String path;

	@Column(name = "level")
	private int level;

	@Column(name = "type")
	private String type;

	@Column(name = "code")
	private String code;

	@Column(name = "name")
	private String name;

	@Column(name = "translate_name")
	private String translateName;

	@Column(name = "status")
	private String status;

	@JsonIgnore
	@OneToOne(mappedBy = "department")
	private User user;
}
