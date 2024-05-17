package com.hust.globalict.main.modules;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.hust.globalict.main.listeners.RoleListener;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "roles")
@EntityListeners(RoleListener.class)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Role {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "name", length = 100, nullable = false, unique = true)
	private String name;
	
	@JsonIgnore
	@OneToMany(mappedBy = "role")
	private List<UserLogin> userLogins;
}
