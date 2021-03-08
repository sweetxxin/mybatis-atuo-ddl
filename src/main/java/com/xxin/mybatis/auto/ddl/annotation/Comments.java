package com.xxin.mybatis.auto.ddl.annotation;

import java.lang.annotation.*;

/**
 * @description:
 * @author: chenyixin7
 * @create: 2021-03-08 09:32
 */
@Target({ElementType.FIELD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Comments {
    String value() default "";
}
