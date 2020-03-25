package com.deng.filter;

import java.util.Arrays;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.StringUtils;

@Slf4j
public class AuthorizeGatewayFilterFactory extends AbstractGatewayFilterFactory<AuthorizeGatewayFilterFactory.Config> {

	private static final String AUTHORIZE_TOKEN = "token";
	
	public AuthorizeGatewayFilterFactory() {
		super(Config.class);
	}
	
	@Override
	public List<String> shortcutFieldOrder() {
		return Arrays.asList("enabled");
	}

	public static class Config {
        // controls whether authentication is turned on
        private boolean enabled;

        public Config() {}

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }

	@Override
	public GatewayFilter apply(Config config) {
		return (exchange, chain) -> {
			if (!config.isEnabled()) {
				return chain.filter(exchange);
			}
			
			ServerHttpRequest request = exchange.getRequest();
	        HttpHeaders headers = request.getHeaders();
	        String token = headers.getFirst(AUTHORIZE_TOKEN);
	        if (token == null) {
	            token = request.getQueryParams().getFirst(AUTHORIZE_TOKEN);
	        }
	        log.info(token);

	        ServerHttpResponse response = exchange.getResponse();
	        if (StringUtils.isEmpty(token)) {
	            response.setStatusCode(HttpStatus.UNAUTHORIZED);
	            return response.setComplete();
	        }
	        
	        // 此处可增加对token校验的微服务
	        
			return chain.filter(exchange);
		};
	}
}
