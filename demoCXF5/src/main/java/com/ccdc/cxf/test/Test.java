package com.ccdc.cxf.test;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;

import com.ccdc.cxf.client.SayCXFClient;

public class Test {
	public static void main(String[] args) {
		JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
		factory.setServiceClass(SayCXFClient.class);
		factory.setAddress("http://localhost:8080/demoCXF5/CXFService/service");
		SayCXFClient service = (SayCXFClient) factory.create();
		System.out.println(service.sayHello("zhouxy"));
	}
}