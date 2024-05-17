package com.hust.globalict.main.modules;

import java.time.LocalDateTime;

import com.hust.globalict.main.listeners.TokenListener;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tokens")
@EntityListeners(TokenListener.class)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Token {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "token", columnDefinition = "TEXT", unique = true, nullable = false)
	private String token;

	@Column(name = "token_type", length = 50, nullable = false)
	private String tokenType;

	@Column(name = "refresh_token")
	private String refreshToken;

	@Column(name = "expiration_date")
	private LocalDateTime expirationDate;

	@Column(name = "refresh_expiration_date")
	private LocalDateTime refreshExpirationDate;

	@Column(name = "is_mobile", columnDefinition = "BOOLEAN")
	private boolean isMobile;

	@Column(name = "revoked", columnDefinition = "BOOLEAN", nullable = false)
	private boolean revoked;

	@Column(name = "expired", columnDefinition = "BOOLEAN", nullable = false)
	private boolean expired;

	@ManyToOne
	@JoinColumn(name = "user_login_id")
	private UserLogin userLogin;
}
