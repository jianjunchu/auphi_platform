package org.firzjb.base.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 在Controller的方法上使用此注解，该方法在映射时会对用户进行身份验证，验证失败返回401错误
 * 也可以直接在Controller上使用，代表该Controller的所有方法均需要身份验证
 * @auther 傲飞数据整合平台
 * @create 2018-09-15 20:07
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Authorization {
}
