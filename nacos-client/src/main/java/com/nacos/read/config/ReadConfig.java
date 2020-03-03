package com.nacos.read.config;

import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.Executor;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;

public class ReadConfig {

	public static void main(String[] args) throws NacosException, IOException {

		String serverAddr = "localhost";
		String dataId = "appA";
		String group = "DEFAULT_GROUP";
		Properties properties = new Properties();
		properties.put("serverAddr", serverAddr);
		ConfigService configService = NacosFactory.createConfigService(properties);
		String content = configService.getConfig(dataId, group, 10);
		System.out.println("first receive: " + content);
		configService.addListener(dataId, group, new Listener() {

			@Override
			public Executor getExecutor() {
				return null;
			}

			@Override
			public void receiveConfigInfo(String configInfo) {
				System.out.println("currentTime: " + new Date() + ", receive: " + configInfo);
			}});
		
		int n = System.in.read();
	}

}
