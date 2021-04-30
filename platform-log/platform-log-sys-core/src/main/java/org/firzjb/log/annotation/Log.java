package org.firzjb.log.annotation;

import java.lang.annotation.*;

/**
 *
 *
 * @auther 制证数据实时汇聚系统
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
