package com.aofei.webservice;

import com.aofei.webservice.controller.HelloWorld;

import javax.jws.WebService;

@WebService(endpointInterface = "com.aofei.webservice.controller.HelloWorld")
public class HelloWorldImpl implements HelloWorld {
    @Override
    public String sayHi(String text) {
        System.out.println("sayHi called");
        return "Hello " + text;

    }
}
