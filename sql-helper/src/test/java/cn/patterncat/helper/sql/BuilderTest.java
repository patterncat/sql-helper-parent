package cn.patterncat.helper.sql;

import cn.patterncat.helper.sql.builder.NamedParamSqlBuilder;
import org.junit.Test;

/**
 * Created by patterncat on 2017-11-21.
 */
public class BuilderTest {

    @Test
    public void testBuilderSql(){
        NamedParamSqlBuilder sql = new NamedParamSqlBuilder("select * from book ")
                .where()
                .append("title = :title","title","hello")
                .and()
                .append("name = :name","name","hello")
                .and()
                .appendIfNotNull("status = :status","status",null);
        System.out.println(sql.getParams());
        System.out.println(sql.build());
    }
}
