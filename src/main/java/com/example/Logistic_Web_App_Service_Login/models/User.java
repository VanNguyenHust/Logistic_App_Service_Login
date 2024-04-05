package com.example.Logistic_Web_App_Service_Login.models;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "users")
@EntityListeners(value = User.class)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class User extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	Long id;

	@Column(name = "full_name", length = 100)
	String fullName;
	
	@Column(name = "first_name", length = 100)
	String firstName;
	
	@Column(name = "last_name", length = 100)
	String lastName;

	@Column(name = "is_active", columnDefinition = "TINYINT(1)")
	short isActive;

	@JsonIgnore
	@OneToMany(mappedBy = "user")
	Set<UserLogin> userLogins;

	@OneToOne
	@JoinColumn(name = "tenant_id")
	Tenant tenant;
}
