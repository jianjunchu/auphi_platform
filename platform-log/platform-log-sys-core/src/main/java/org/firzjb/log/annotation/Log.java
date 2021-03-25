package org.firzjb.log.annotation;

import java.lang.annotation.*;

/**
 *
 *
 * @auther 傲飞数据整合平台
 * @create 2018-09-12 15:25
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {

    /**模块*/
    String module() default "";

    /**描述*/
    String description() default "";
}
