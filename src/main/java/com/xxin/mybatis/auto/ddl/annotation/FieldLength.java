package com.xxin.mybatis.auto.ddl.annotation;

import java.lang.annotation.*;

/**
 * @description:
 * @author: chenyixin7
 * @create: 2021-03-08 09:58
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FieldLength {
    int value() default 255;
}
