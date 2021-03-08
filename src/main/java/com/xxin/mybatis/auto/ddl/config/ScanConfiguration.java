package com.xxin.mybatis.auto.ddl.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @description:
 * @author: chenyixin7
 * @create: 2021-03-05 11:50
 */
@Configuration
@ComponentScan(basePackages = {"com.xxin.mybatis.auto.ddl"})
@MapperScan({"${starfish.data.jdbc.mybatis-plus.mapper-scan:com.xxin.mybatis.auto.ddl.mapper*}"})
public class ScanConfiguration {
}
