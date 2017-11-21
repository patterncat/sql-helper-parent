package cn.patterncat.jdbc.query;

import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.sql.RelationalPathBase;

/**
 * Created by patterncat on 2017-11-21.
 */
public class QBase<T> extends RelationalPathBase<T> {
    public QBase(Class<? extends T> type, String variable, String schema, String table){
        super(type,variable,schema,table);
    }

    public QBase(Class<? extends T> type, PathMetadata metadata, String schema, String table){
        super(type,metadata,schema,table);
    }

    public StringPath getFieldString(String name){
        return createString(name);
    }
    public NumberPath<Integer> getFieldNumber(String name){
        return createNumber(name, Integer.class);
    }

    public NumberPath<Long> getFieldNumberLong(String name){
        return createNumber(name, Long.class);
    }
}
