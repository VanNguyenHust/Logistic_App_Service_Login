package com.hust.globalict.main.modules;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.hust.globalict.main.listeners.TenantListener;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tenants")
@EntityListeners(TenantListener.class)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Tenant extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "code", length = 100, nullable = false, unique = true)
	private String code;

	@Column(name = "name", length = 100, nullable = false)
	private String name;

	@Column(name = "translate_name", length = 100, nullable = false)
	private String translateName;

	@Column(name = "status", length = 100, nullable = false)
	private String status;

	@JsonIgnore
	@OneToOne(mappedBy = "tenant")
	private User user;
}
