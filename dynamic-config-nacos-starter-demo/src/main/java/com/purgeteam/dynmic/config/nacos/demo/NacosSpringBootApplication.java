package com.purgeteam.dynmic.config.nacos.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.purgeteam.dynamic.config.starter.annotation.EnableDynamicConfigEvent;

@EnableDynamicConfigEvent
@SpringBootApplication
public class NacosSpringBootApplication {

  public static void main(String[] args) {
    SpringApplication.run(NacosSpringBootApplication.class, args);
  }

}
