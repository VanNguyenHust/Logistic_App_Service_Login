package com.hust.globalict.main.configurations;

import com.hust.globalict.main.components.LocalizationUtils;
import com.hust.globalict.main.exceptions.DataNotFoundException;
import com.hust.globalict.main.modules.UserLogin;
import com.hust.globalict.main.utils.MessageKeyExceptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.hust.globalict.main.repositories.UserLoginRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
	private final UserLoginRepository userLoginRepository;

	private final LocalizationUtils localizationUtils;

	@Bean
	UserDetailsService userDetailsService() {
		return username -> {
			UserLogin userLogin = userLoginRepository.findByUsername(username);
			if (userLogin == null) {
                try {
                    throw new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeyExceptions.USER_LOGIN_NOT_FOUND));
                } catch (DataNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }

			return userLogin;
		};
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
}
