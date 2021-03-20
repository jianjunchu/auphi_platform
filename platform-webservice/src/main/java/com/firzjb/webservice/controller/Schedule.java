package com.firzjb.webservice.controller;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService
public interface Schedule {


    @WebMethod
    Boolean execute(@WebParam(name="jobName")String jobName);
}
