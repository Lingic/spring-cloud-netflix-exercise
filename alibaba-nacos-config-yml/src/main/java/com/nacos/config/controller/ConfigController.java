package com.nacos.config.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executor;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;

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
    
    @GetMapping("/helloDynamic")
    public String helloDynamic() throws NacosException {
    	String serverAddr = "localhost:8848";
    	String dataId = "nacos-config";
    	String group = "DEFAULT_GROUP";
    	Properties properties = new Properties();
    	properties.put("serverAddr", serverAddr);
    	ConfigService configService = NacosFactory.createConfigService(properties);
    	String content = configService.getConfig(dataId, group, 5000);
    	log.info("content = ", content);
    	
    	List<Object> list = new ArrayList<>();
    	configService.addListener(dataId, group, new Listener() {
    		@Override
    		public void receiveConfigInfo(String configInfo) {
    			//System.out.println("recieve1:" + configInfo);
    			log.info("recieve1:" + configInfo);
    			list.add(configInfo);
    		}
    		@Override
    		public Executor getExecutor() {
    			return null;
    		}
    	});
    	
    	return "helloDynamic";
    }
}
