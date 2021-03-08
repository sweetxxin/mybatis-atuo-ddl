package com.xxin.mybatis.auto.ddl.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @description:
 * @author: chenyixin7
 * @create: 2021-03-05 09:40
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TableDefine {
    private String name;
    private String comment;
    private List<ColumnDefine> columns;
    private String pk;
}
