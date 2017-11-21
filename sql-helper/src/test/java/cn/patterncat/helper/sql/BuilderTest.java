package cn.patterncat.helper.sql;

import cn.patterncat.helper.sql.builder.NamedParamSqlBuilder;
import cn.patterncat.helper.sql.criteria.Restrictions;
import cn.patterncat.helper.sql.criteria.WhereClause;
import org.junit.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Map;
import java.util.Objects;

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

    @Test
    public void testCriteria(){
        Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC,"createdAt"),
                new Sort.Order(Sort.Direction.ASC,"id"));
        WhereClause whereClause = WhereClause
                .newInstance("book")
                .add(Restrictions.equalTo("title","this is title"))
                .add(Restrictions.equalTo("id",123L))
                .page(new PageRequest(0,10,sort));

        System.out.println(whereClause.toSql());
        System.out.println(whereClause.getPageSql());
        System.out.println(whereClause.toSelectSql());
        System.out.println(whereClause.toCountSql());

        Map<String,Object> params = whereClause.getNamedParams();
        for(Map.Entry<String,Object> entry : params.entrySet()){
            System.out.println("key:"+entry.getKey() + "," + entry.getValue());
        }
    }
}
