package com.xxin.mybatis.auto.ddl.pojo;

import lombok.Builder;
import lombok.Data;

/**
 * @description:
 * @author: chenyixin7
 * @create: 2021-03-05 09:40
 */
@Data
@Builder
public class ColumnDefine {
    private String name;
    private String underlineName;
    private String javaType;
    private String dbType;
    private Integer length;
    private String comment;
    private boolean isPrimary;
    private boolean isUnique;
}
