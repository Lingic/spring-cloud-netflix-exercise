package com.deng;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

import com.deng.filter.AuthorizeGatewayFilterFactory;

@SpringBootApplication
@EnableDiscoveryClient
public class GatewayServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayServerApplication.class, args);
    }

//    @Bean
//    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
//		return builder.routes().route(r -> 
//				r.path("/hi")
//				.uri("http://localhost:8001/hi")
//				.filter(new AuthorizeGatewayFilter())
//				.id("user-service"))
//			.build();
//    }
    
    @Bean
    public AuthorizeGatewayFilterFactory authorizeGatewayFilterFactory() {
    	return new AuthorizeGatewayFilterFactory();
    }
}
