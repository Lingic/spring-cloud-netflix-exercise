package com.gateway;

import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class GatewayServerApplication {

    public static void main(String[] args) {
    	log.info("GatewayServerApplication");
        SpringApplication.run(GatewayServerApplication.class, args);
    }

}
