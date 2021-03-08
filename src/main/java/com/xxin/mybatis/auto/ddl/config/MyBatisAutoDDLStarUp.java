package com.xxin.mybatis.auto.ddl.config;


import com.xxin.mybatis.auto.ddl.mapper.TableMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @description:
 * @authr: chenyixin7
 * @create: 2021-03-05 10:41
 */
@Component
@Slf4j
public class MyBatisAutoDDLStarUp implements CommandLineRunner {
    private TableMapper mapper;

    public MyBatisAutoDDLStarUp(TableMapper createTableMapper){
        mapper = createTableMapper;
    }
    @Override
    public void run(String... args) throws Exception {
        String ddlStrategy = MyBatisAutoDDLDefinitionRegistry.ddlStrategy;
        if (StringUtils.isNotBlank(ddlStrategy)){
            log.info("自动生成表,策略：{}",ddlStrategy);
            if (!ddlStrategy.equals("none")){
                List<String> ddl = MyBatisAutoDDLDefinitionRegistry.ddl;
                for (String s : ddl) {
                    log.info(s);
                }
                mapper.doDDL(ddl);
            }
        }

    }
}
