package com.xxin.mybatis.auto.ddl.annotation;

/**
 * @description:
 * @author: chenyixin7
 * @create: 2021-03-05 09:54
 */
public @interface EnableMyBatisAutoDDL {
    String basePackages() default "";
    String ddl() default "none";
}
