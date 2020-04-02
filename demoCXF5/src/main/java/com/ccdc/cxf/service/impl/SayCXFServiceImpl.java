package com.ccdc.cxf.service.impl;

import javax.jws.WebService;

import com.ccdc.cxf.service.SayCXFService;

@WebService(endpointInterface = "com.ccdc.cxf.service.SayCXFService", targetNamespace = "http://service.cxf.ccdc.com/")
public class SayCXFServiceImpl implements SayCXFService {
	static{
		System.out.println("test!!!");
	}
	public String sayHello(String name) {
		return name + " hello,this is my first CXF webService!";
	}
}
