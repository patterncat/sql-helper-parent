package cn.patterncat.helper.sql;

import com.querydsl.core.types.Path;
import com.querydsl.sql.*;
import org.junit.Test;

/**
 * Created by patterncat on 2017-11-20.
 */
public class QueryDslTest {

    @Test
    public void testGenSql(){
        SQLTemplates templates = PostgreSQLTemplates.builder()
                .printSchema()
                .build();
        Configuration configuration = new Configuration(templates);

//        Path<Object> userPath = new RelationalPathBase<Object>(Object.class, variable, schema, table);

        SQLQuery sqlQuery = new SQLQuery(configuration);

    }
}
