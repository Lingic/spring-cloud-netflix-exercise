package com.nacos.config.dynamic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;

@Component
public class DynamicConfigListener implements ApplicationEventPublisherAware {

	private final Logger log = LoggerFactory.getLogger(DynamicConfigListener.class);
	
	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher publisher) {
		log.info(publisher.toString());
	}

}
