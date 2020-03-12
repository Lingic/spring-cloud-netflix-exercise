package com.purgeteam.dynmic.config.nacos.demo;

import lombok.extern.slf4j.Slf4j;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.purgeteam.dynamic.config.starter.event.ActionConfigEvent;

/**
 * @author purgeyao
 * @since 1.0
 */
@Slf4j
@Component
public class NacosListener implements ApplicationListener<ActionConfigEvent> {

	@Override
	public void onApplicationEvent(ActionConfigEvent event) {
		log.info("接收事件");
		log.info(event.getPropertyMap().toString());
	}
}
