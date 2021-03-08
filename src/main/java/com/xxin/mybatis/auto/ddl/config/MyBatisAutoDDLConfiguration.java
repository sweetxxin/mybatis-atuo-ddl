package com.xxin.mybatis.auto.ddl.config;


import com.xxin.mybatis.auto.ddl.mapper.TableMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description:
 * @author: chenyixin7
 * @create: 2020-07-30 11:48
 */
@ConditionalOnClass(TableMapper.class)
@Configuration
public class MyBatisAutoDDLConfiguration {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Bean
    public MyBatisAutoDDLDefinitionRegistry myBatisAutoDDLDefinitionRegistry() {
        return new MyBatisAutoDDLDefinitionRegistry();
    }
    @Bean
    @ConditionalOnClass(TableMapper.class)
    public MyBatisAutoDDLStarUp myBatisAutoDDLStarUp(TableMapper createTableMapper){
        return new MyBatisAutoDDLStarUp(createTableMapper);
    }
}
