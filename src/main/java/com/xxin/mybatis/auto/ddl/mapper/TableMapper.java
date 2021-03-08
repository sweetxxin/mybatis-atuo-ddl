package com.xxin.mybatis.auto.ddl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @description:
 * @author: chenyixin7
 * @create: 2021-03-05 10:30
 */
@Mapper
public interface TableMapper{
    void doDDL(@Param("tables") List<String> tables);
}
