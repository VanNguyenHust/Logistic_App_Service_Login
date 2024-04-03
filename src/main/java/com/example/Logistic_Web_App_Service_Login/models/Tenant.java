package com.example.Logistic_Web_App_Service_Login.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "tenants")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class Tenant extends BaseEntity{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	Long id;
	
	@Column(name = "code", length = 100, nullable = false, unique = true)
	String code;
	
	@Column(name = "name", length = 100, nullable = false, unique = true)
	String name;
	
	@Column(name = "translate_name", length = 100, nullable = false, unique = true)
	String translateName;
	
	@Column(name = "status", length = 100, nullable = false, unique = true)
	String status;
	
	@JsonIgnore
	@OneToOne(mappedBy = "tenant")
	User user;
}
