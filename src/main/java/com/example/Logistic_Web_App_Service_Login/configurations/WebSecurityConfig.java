package com.example.Logistic_Web_App_Service_Login.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.security.reactive.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.example.Logistic_Web_App_Service_Login.filters.JwtTokenFilter;

import lombok.RequiredArgsConstructor;

@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebMvc
@RequiredArgsConstructor
public class WebSecurityConfig {
	private final JwtTokenFilter jwtTokenFilter;

	@Value("${api.prefix}")
	private String apiPrefix;

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
				.authorizeHttpRequests(requests -> {
					requests.requestMatchers(String.format("%s/user-login/register", apiPrefix),
							String.format("%s/user-login", apiPrefix),
							String.format("%s/user-login/reset-password**", apiPrefix),
							// healthcheck
							String.format("%s/healthcheck/**", apiPrefix),

							// swagger
							// "/v3/api-docs",
							// "/v3/api-docs/**",
							"/api-docs", "/api-docs/**", "/swagger-resources", "/swagger-resources/**",
							"/configuration/ui", "/configuration/security", "/swagger-ui/**", "/swagger-ui.html",
							"/webjars/swagger-ui/**", "/swagger-ui/index.html"

				).permitAll().requestMatchers(HttpMethod.GET, String.format("%s/role**", apiPrefix)).permitAll()

							.anyRequest().authenticated();
				}).csrf(AbstractHttpConfigurer::disable);
		http.securityMatcher(String.valueOf(EndpointRequest.toAnyEndpoint()));
		return http.build();
	}
}
