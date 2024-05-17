package com.hust.globalict.main.modules;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.hust.globalict.main.listeners.UserListener;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@EntityListeners(UserListener.class)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "full_name", length = 100)
	private String fullName;

	@Column(name = "first_name", length = 100)
	private String firstName;

	@Column(name = "last_name", length = 100)
	private String lastName;

	@Column(name = "phone")
	private String phone;

	@Column(name = "is_active", columnDefinition = "smallint")
	private short isActive = 1;

	@JsonIgnore
	@OneToMany(mappedBy = "user")
	private Set<UserLogin> userLogins;

	@OneToOne
	@JoinColumn(name = "tenant_id")
	private Tenant tenant;

	@OneToOne
	@JoinColumn(name = "department_id")
	private Department department;
}
