package com.example.Logistic_Web_App_Service_Login.models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "tokens")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class Token {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	Long id;
	
	@Column(name = "token", length = 255, unique = true, nullable = false)
	String token;
	
	@Column(name = "token_type", length = 50, nullable = false)
	String tokenType;
	
	@Column(name = "refresh_token", length = 255)
    private String refreshToken;
	
	@Column(name = "expiration_date")
	LocalDateTime expirationDate;
	
	@Column(name = "refresh_expiration_date")
    private LocalDateTime refreshExpirationDate;
	
	@Column(name = "is_mobile", columnDefinition = "TINYINT(1)")
    private boolean isMobile;
	
	@Column(name = "revoked", columnDefinition = "TINYINT(1)", nullable = false)
	boolean revoked;

	@Column(name = "expired", columnDefinition = "TINYINT(1)", nullable = false)
	boolean expired;
	
	@ManyToOne
	@JoinColumn(name = "user_login_id")
	UserLogin userLogin;
}
