package org.firzjb.dataservice.exception;

public enum DataServiceError {

    USERNAME_EXIST(700001, "用户名被占用"),//用户名被占用
    LOGIN_FAILED(700002, "用户名或密码错误"),//用户名或密码错误
    SERVICE_AUTH_EXISTING(700003, "用户服务授权已存在"),//用户服务授权已存在
    PERMISSION_DENIED(700004, "用户名或密码错误");//用户名或密码错误
    /*状态码*/
    private int code;
    /*信息*/
    private String message;

    DataServiceError(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
