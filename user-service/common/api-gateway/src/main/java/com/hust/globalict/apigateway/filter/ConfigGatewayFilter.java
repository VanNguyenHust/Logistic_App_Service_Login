package com.hust.globalict.apigateway.filter;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

import com.hust.globalict.apigateway.components.JwtTokenUtil;

@Component
public class ConfigGatewayFilter extends AbstractGatewayFilterFactory<ConfigGatewayFilter.Config> {
	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	public ConfigGatewayFilter() {
		super(Config.class);
	}

	@Override
	public GatewayFilter apply(Config config) {
		return (exchange, chain) -> {
			String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");

			if (authHeader != null) {
				String token = authHeader.substring(7);

				Map<String, String> data = jwtTokenUtil.extractUser(token);
				
				data.forEach((key, value) -> {
				    exchange.getRequest().mutate().headers(httpHeaders -> {
				        httpHeaders.add(key, value);
				    });
				});
			}
			
			return chain.filter(exchange);
		};

	}

	public static class Config {
		// Đặt bất kỳ thuộc tính cấu hình nào bạn muốn ở đây
	}
}
