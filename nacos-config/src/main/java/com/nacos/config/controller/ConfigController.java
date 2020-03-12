package com.nacos.config.controller;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RefreshScope
public class ConfigController {

	@Value("${nacos.config}")
    private String config;

    @GetMapping("/helloConfig")
    public String helloConfig(){
    	log.info("hello config " + config);
        return config;
    }
}
