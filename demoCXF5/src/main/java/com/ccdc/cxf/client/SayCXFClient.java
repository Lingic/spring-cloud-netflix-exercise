package com.ccdc.cxf.client;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

@WebService(
targetNamespace = "http://service.cxf.ccdc.com/")
public interface SayCXFClient {
@WebMethod
@WebResult
//下面的name="wsdl中暴露出来的接口参数名称"
public  String sayHello(@WebParam(name="name",targetNamespace = "http://service.cxf.ccdc.com/") String name);
}
