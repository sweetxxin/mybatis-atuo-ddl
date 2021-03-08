package com.xxin.mybatis.auto.ddl.util;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import com.xxin.mybatis.auto.ddl.annotation.Comments;
import com.xxin.mybatis.auto.ddl.annotation.FieldLength;
import com.xxin.mybatis.auto.ddl.command.JavaToDBType;
import com.xxin.mybatis.auto.ddl.pojo.ColumnDefine;
import com.xxin.mybatis.auto.ddl.pojo.TableDefine;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @description:
 * @author: chenyixin7
 * @create: 2021-03-05 09:26
 */
public class Util {
    private static Pattern humpPattern = Pattern.compile("[A-Z]");

    public static List<TableDefine> parseTableEntity(Set<Class<?>> classes){
        List<TableDefine> tableDefines = new ArrayList<>();
        for (Class<?> table : classes) {
            TableDefine tableDefine = new TableDefine();
            List<ColumnDefine> columnDefines = new ArrayList<>();
            TableName tableAnnotation = table.getAnnotation(TableName.class);
            String tableName = tableAnnotation.value();
            tableDefine.setName(tableName);
            if (table.isAnnotationPresent(Comments.class)){
                tableDefine.setComment(table.getAnnotation(Comments.class).value());
            }
            Field[] declaredFields = table.getDeclaredFields();
            for (Field field : declaredFields) {
                if (field.isAnnotationPresent(TableId.class)){
                    tableDefine.setPk(field.getName());
                }
                String colName = field.getName();

                if (field.isAnnotationPresent(TableField.class)){
                    TableField column = field.getAnnotation(TableField.class);
                    if (column.exist()){
                        colName = StringUtils.isBlank(column.value())?colName:column.value();
                    }else{
                        colName = null;
                    }
                }
                if (colName!=null){
                    String msg = "";
                    if (field.isAnnotationPresent(Comments.class)){
                        Comments comments = field.getAnnotation(Comments.class);
                        msg = comments.value();
                    }
                    int length = 255;
                    if (field.isAnnotationPresent(FieldLength.class)){
                        length = field.getAnnotation(FieldLength.class).value();
                    }
                    ColumnDefine build = ColumnDefine.builder()
                            .name(colName)
                            .underlineName(humpToLine(colName))
                            .javaType(field.getType().getName())
                            .comment(msg)
                            .dbType(JavaToDBType.javaToPgSqlTypeMap.get(field.getType().getName()))
                            .length(length).build();
                    columnDefines.add(build);
                }
            }
            tableDefine.setColumns(columnDefines);
            tableDefines.add(tableDefine);
        }
        return tableDefines;
    }

    public static List<String> createTableDDL(List<TableDefine> tableDefines,String strategy){
        if (strategy.equals("none")){
            return new ArrayList<>();
        }

        List<String> ddlSql = new ArrayList<>(tableDefines.size());
        List<String> comments = new ArrayList<>();
        for (TableDefine tableDefine : tableDefines) {
            if (strategy.equals("create")){
                ddlSql.add(System.getProperty("line.separator")+"DROP TABLE IF EXISTS "+tableDefine.getName()+";");
            }
            if (StringUtils.isNotBlank(tableDefine.getComment())){
                String comment = " comment on table "+tableDefine.getName()+" is '’"+tableDefine.getComment()+"';";
                comments.add(comment);
            }
            StringBuilder ddl = new StringBuilder();
            ddl.append(System.getProperty("line.separator"));
            ddl.append("CREATE TABLE IF NOT EXISTS ").append(tableDefine.getName());
            ddl.append("(").append(System.getProperty("line.separator"));
            for (ColumnDefine column : tableDefine.getColumns()) {
                ddl.append(column.getUnderlineName()).append(" ").append(column.getDbType());
                if (column.getDbType().contains("varchar")){
                    ddl.append("(").append(column.getLength()).append(")");
                }
                if (StringUtils.isNotBlank(column.getComment())){
                    String comment = " comment on column "+tableDefine.getName()+"."+column.getUnderlineName()+" is '"+column.getComment()+"';";
                    comments.add(comment);
                }
                ddl.append(",").append(System.getProperty("line.separator"));
            }
            ddl.append("CONSTRAINT ").append(tableDefine.getName()).append("_pKey").append(" PRIMARY KEY").append("(").append(humpToLine(tableDefine.getPk())).append(")");
            ddl.append(");");
            ddlSql.add(ddl.toString());
        }
        ddlSql.addAll(comments);
        return ddlSql;
    }
    /** 驼峰转下划线,效率比上面高 */
    public static   String humpToLine(String str) {
        Matcher matcher = humpPattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
}
