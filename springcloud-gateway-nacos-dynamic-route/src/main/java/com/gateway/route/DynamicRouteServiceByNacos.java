package com.gateway.route;

import java.util.concurrent.Executor;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;

@Slf4j
@Component
public class DynamicRouteServiceByNacos {

	@Autowired
	private DynamicRouteService dynamicRouteService;
	
	public DynamicRouteServiceByNacos() {
		dynamicRouteByNacosListener("sc-gateway","xujin_test");
	}
	
	public void dynamicRouteByNacosListener (String dataId, String group){
        try {
            ConfigService configService=NacosFactory.createConfigService("127.0.0.1:8848");
            String content = configService.getConfig(dataId, group, 5000);
            log.info("content = " + content);
            System.out.println(content);
            configService.addListener(dataId, group, new Listener()  {
                @Override
                public void receiveConfigInfo(String configInfo) {
                    RouteDefinition definition= JSON.parseObject(configInfo,RouteDefinition.class);
                    log.info("definition = " + JSON.toJSONString(definition));
                    System.out.println(JSON.toJSONString(definition));
                    dynamicRouteService.update(definition);
                }
                @Override
                public Executor getExecutor() {
                    return null;
                }
            });
        } catch (NacosException e) {
        	log.error("Error", e);
        }
    }
}
