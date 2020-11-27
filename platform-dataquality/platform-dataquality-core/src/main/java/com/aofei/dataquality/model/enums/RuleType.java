package com.aofei.dataquality.model.enums;

/**
 * 数据稽核类型
 */
public  enum   RuleType {


    IS_EMPTY(1000, "空值判断"),
    IS_NOT_EMPTY(2000, "非空值判断"),
    EMAIL_CHECK(3000, "邮箱检查"),
    MOBILE_CHECK(4000, "手机检查"),
    ID_CARD_CHECK(5000, "身份证验证"),
    NUM_RANGE_CHECK(6000, "数值范围检查"),
    DATE_RANGE_CHECK(7000, "日期范围检查"),
    REGEX_CHECK(8000, "自定义正则检查");


    /*状态码*/
    private Integer code;
    /*信息*/
    private String message;



    RuleType(int code, String message ) {
        this.code = code;
        this.message = message;

    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}

