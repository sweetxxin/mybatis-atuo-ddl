package com.xxin.mybatis.auto.ddl.command;

import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: chenyixin7
 * @create: 2021-03-05 09:59
 */
public class JavaToDBType {

    public static Map<String, String> javaToPgSqlTypeMap = new HashMap();
        public JavaToDBType() {
        }

        static {
            javaToPgSqlTypeMap.put("java.lang.String", "varchar");
            javaToPgSqlTypeMap.put("java.lang.Long", "bigint");
            javaToPgSqlTypeMap.put("java.lang.Integer", "int");
            javaToPgSqlTypeMap.put("java.lang.Boolean", "bit");
            javaToPgSqlTypeMap.put("java.math.BigInteger", "bigint");
            javaToPgSqlTypeMap.put("java.lang.Float", "float");
            javaToPgSqlTypeMap.put("java.lang.Double", "double");
            javaToPgSqlTypeMap.put("java.math.BigDecimal", "decimal");
            javaToPgSqlTypeMap.put("java.sql.Date", "date");
            javaToPgSqlTypeMap.put("java.util.Date", "date");
            javaToPgSqlTypeMap.put("java.sql.Timestamp", "datetime");
            javaToPgSqlTypeMap.put("java.sql.Time", "time");
            javaToPgSqlTypeMap.put("long", "bigint");
            javaToPgSqlTypeMap.put("int", "int");
            javaToPgSqlTypeMap.put("boolean", "bit");
            javaToPgSqlTypeMap.put("float", "float");
            javaToPgSqlTypeMap.put("double", "double");
            javaToPgSqlTypeMap.put("byte", "tinyint");
            javaToPgSqlTypeMap.put("short", "smallint");
            javaToPgSqlTypeMap.put("char", "varchar");
        }
}

