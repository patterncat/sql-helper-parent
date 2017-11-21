package cn.patterncat.helper.sql.util;

import cn.patterncat.helper.sql.builder.SQL;

import java.util.Map;

/**
 * Created by patterncat on 2017-11-21.
 */
public class SqlUtils {

    public static String buildInsertNamedSql(String table,Map<String,Object> valueMap,boolean selective){
        SQL sql = new SQL();
        sql.INSERT_INTO(table);
        for(Map.Entry<String,Object> entry:valueMap.entrySet()){
            String key = entry.getKey();
            Object value = entry.getValue();
            String column = ValueUtils.camelhumpToUnderline(key);
            if(selective){
                if(value != null){
                    sql.VALUES(column, ":" + key);
                }
            }else{
                sql.VALUES(column, ":" + key);
            }
        }
        return sql.toString();

    }

    public static String buildUpdateNamedSql(String table,Map<String,Object> valueMap,boolean selective){
        SQL sql = new SQL();
        sql.UPDATE(table);

        for(Map.Entry<String,Object> entry:valueMap.entrySet()){
            String key = entry.getKey();
            String column = ValueUtils.camelhumpToUnderline(key);
            if("id".equals(key)){
                continue;
            }
            Object value = entry.getValue();
            if(selective){
                if(value != null){
                    sql.SET(column + " = :" + key);
                }
            }else{
                sql.SET(column + " = :" + key);
            }
        }
        return sql.toString();
    }
}
